package com.example.gadgetariumb8.db.service.impl;

import com.example.gadgetariumb8.db.dto.request.ProductRequest;
import com.example.gadgetariumb8.db.dto.request.SubProductRequest;
import com.example.gadgetariumb8.db.dto.response.PaginationResponse;
import com.example.gadgetariumb8.db.dto.response.ProductAdminResponse;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.example.gadgetariumb8.db.exception.exceptions.NotFoundException;
import com.example.gadgetariumb8.db.model.Brand;
import com.example.gadgetariumb8.db.model.Product;
import com.example.gadgetariumb8.db.model.SubCategory;
import com.example.gadgetariumb8.db.model.SubProduct;
import com.example.gadgetariumb8.db.repository.BrandRepository;
import com.example.gadgetariumb8.db.repository.SubCategoryRepository;
import com.example.gadgetariumb8.db.repository.SubProductRepository;
import com.example.gadgetariumb8.db.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final SubCategoryRepository subCategoryRepository;
    private final BrandRepository brandRepository;
    private final SubProductRepository subProductRepository;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public SimpleResponse saveProduct(ProductRequest productRequest) {
        SubCategory subCategory = subCategoryRepository.findById(productRequest.subCategoryId())
                .orElseThrow(() -> new NotFoundException("Sub category with id:" + productRequest.subCategoryId() + " not found!!"));

        Brand brand = brandRepository.findById(productRequest.brandId())
                .orElseThrow(() -> new NotFoundException("Brand with id:" + productRequest.brandId() + " not found!!!"));
        Product product = new Product();
        product.setSubCategory(subCategory);
        product.setBrand(brand);
        product.setGuarantee(productRequest.guarantee());
        product.setName(productRequest.name());
        product.setDateOfIssue(productRequest.dateOfIssue());
        product.setVideo(productRequest.video());
        product.setPDF(productRequest.PDF());
        product.setDescription(productRequest.description());
        for (SubProductRequest s : productRequest.subProducts()) {
            SubProduct subProduct = new SubProduct();
            subProduct.addCharacteristics(s.characteristics());
            subProduct.setColour(s.colour());
            subProduct.setPrice(s.price());
            subProduct.setQuantity(s.quantity());
            subProduct.setImages(s.images());
            subProduct.setProduct(product);
            product.addSubProduct(subProduct);
            subProductRepository.save(subProduct);
        }
        return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("Successfully saved!!").build();
    }

    @Override
    public PaginationResponse<ProductAdminResponse> getAll(String keyWord, String status, LocalDate from, LocalDate before, String sortBy, int page, int pageSize) {
        String sql = """
                SELECT DISTINCT p.id, (
                    SELECT i.images FROM sub_product_images i WHERE i.sub_product_id = s.id LIMIT 1
                ) AS image, s.item_number, p.name, p.created_at, s.quantity, s.price,
                COALESCE(d.percent, 0) AS percent,
                CASE WHEN d.percent IS NOT NULL THEN ROUND(s.price - (s.price * d.percent / 100)) 
                ELSE s.price END AS total_price
                FROM sub_products s
                JOIN products p ON p.id = s.product_id
                %s
                LEFT JOIN sub_product_characteristics ch ON ch.sub_product_id = s.id
                %s JOIN discounts d ON d.id = p.discount_id
                WHERE %s %s 
                %s
                """;
        String sqlStatus = switch (status) {
            case "on sale" -> "JOIN orders_sub_products o ON o.sub_products_id = s.id";
            case "in favorites" -> "JOIN users_favorites f ON f.favorites_id = s.id";
            case "in basket" -> "JOIN user_basket b ON b.basket_key = s.id";
            default -> "";
        };

        String dateClause = "";
        if (from != null && before != null) {
            dateClause = String.format("AND p.created_at BETWEEN '%s' AND '%s'", from, before);
        } else if (from != null) {
            dateClause = "AND p.created_at >= '%s'".formatted(from);
        } else if (before != null) {
            dateClause = "AND p.created_at <= '%s'".formatted(before);
        }

        List<Object> params = new ArrayList<>();
        String keywordCondition = "1=1";
        if (keyWord != null) {
            params.add("%" + keyWord + "%");
            params.add("%" + keyWord + "%");
            params.add("%" + keyWord + "%");
            params.add("%" + keyWord + "%");
            params.add("%" + keyWord + "%");

            keywordCondition = """
                    p.name iLIKE ? OR p.item_number iLIKE ?
                    OR p.description iLIKE ? OR CAST(s.price AS TEXT) iLIKE ?
                    OR ch.characteristics iLIKE ?
                    """;
        }
        String joinType = "LEFT";
        String orderBy = "";
        switch (sortBy) {
            case "novelties" -> orderBy = "AND p.created_at >= CURRENT_DATE - interval '7 day'";
            case "all promotions" -> joinType = "";
            case "up to 50%" -> orderBy = "AND d.percent < 50";
            case "over 50%" -> orderBy = "AND d.percent >= 50";
            case "recommended" -> orderBy = "AND p.rating >= 4";
            case "by price asc" -> orderBy = "ORDER BY s.price";
            case "by price desc" -> orderBy = "ORDER BY s.price DESC";
        }

        sql = String.format(sql, sqlStatus, joinType, keywordCondition, dateClause, orderBy);

        String countSql = "SELECT COUNT(*) FROM (" + sql + ") as count_query";
        int count = jdbcTemplate.queryForObject(countSql, params.toArray(), Integer.class);
        int totalPage = (int) Math.ceil((double) count / pageSize);

        sql = sql + " LIMIT ? OFFSET ?";
        int offset = (page - 1) * pageSize;
        params.add(pageSize);
        params.add(offset);
        List<ProductAdminResponse> products = jdbcTemplate.query(sql, params.toArray(), (resultSet, i) -> {
            return new ProductAdminResponse(
                    resultSet.getLong("id"),
                    resultSet.getString("image"),
                    resultSet.getString("item_number"),
                    resultSet.getString("name"),
                    LocalDate.parse(resultSet.getString("created_at")),
                    resultSet.getInt("quantity"),
                    resultSet.getBigDecimal("price"),
                    resultSet.getInt("percent"),
                    resultSet.getBigDecimal("total_price")
            );
        });

        return PaginationResponse.<ProductAdminResponse>builder()
                .elements(products)
                .currentPage(page)
                .totalPages(totalPage)
                .build();
    }
}