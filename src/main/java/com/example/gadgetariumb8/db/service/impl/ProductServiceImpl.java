package com.example.gadgetariumb8.db.service.impl;

import com.example.gadgetariumb8.db.dto.request.ProductRequest;
import com.example.gadgetariumb8.db.dto.request.ProductUserRequest;
import com.example.gadgetariumb8.db.dto.request.SubProductRequest;
import com.example.gadgetariumb8.db.dto.response.*;
import com.example.gadgetariumb8.db.exception.exceptions.BadRequestException;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    public PaginationResponse<ProductsResponse> getAllDiscountProducts(int page, int pageSize) {
        String sql = """
                       SELECT (select i.images from sub_product_images i where i.sub_product_id = sp.id limit 1) as image, sp.quantity as quantity, CONCAT(c.name, ' ', p.brand_id, ' ', p.name, ' ',characteristics,' ', sp.colour) as product_info, p.rating as rating, sp.price as price,
                        CAST(sp.price - ((sp.price * d.percent) / 100) AS INTEGER) as discount
                FROM products p
                    JOIN sub_products sp ON p.id = sp.product_id
                    JOIN discounts d ON p.discount_id = d.id
                    JOIN sub_categories sc ON p.sub_category_id = sc.id
                    JOIN categories c ON sc.category_id = c.id
                    JOIN sub_product_characteristics spc ON sp.id = spc.sub_product_id
                WHERE  p.discount_id is not null and characteristics_key like 'memory'
                       """;

        String countSql = "SELECT COUNT(*) FROM (" + sql + ") as count_query";
        int count = jdbcTemplate.queryForObject(countSql, Integer.class);
        int totalPage = (int) Math.ceil((double) count / pageSize);

        int offset = (page - 1) * pageSize;
        sql = String.format(sql + "LIMIT %s OFFSET %s", pageSize, offset);
        List<ProductsResponse> products = jdbcTemplate.query(sql, (resultSet, i) -> new ProductsResponse(
                resultSet.getString("image"),
                resultSet.getInt("quantity"),
                resultSet.getString("product_info"),
                resultSet.getDouble("rating"),
                resultSet.getBigDecimal("price"),
                resultSet.getInt("discount")
        ));
        return PaginationResponse.<ProductsResponse>builder()
                .elements(products)
                .currentPage(page)
                .totalPages(totalPage)
                .build();
    }

    @Override
    public PaginationResponse<ProductsResponse> getNewProducts(int page, int pageSize) {
        String sql = """
                       SELECT (select i.images from sub_product_images i where i.sub_product_id = sp.id limit 1) as image, sp.quantity as quantity, CONCAT(c.name, ' ', p.brand_id, ' ', p.name, ' ',characteristics,' ', sp.colour) as product_info, p.rating as rating, sp.price as price,
                       coalesce(CAST(sp.price - ((sp.price * d.percent) / 100) AS INTEGER),0) as discount
                FROM products p
                    JOIN sub_products sp ON p.id = sp.product_id
                    LEFT JOIN discounts d ON p.discount_id = d.id
                    JOIN sub_categories sc ON p.sub_category_id = sc.id
                    JOIN categories c ON sc.category_id = c.id
                    JOIN sub_product_characteristics spc ON sp.id = spc.sub_product_id
                WHERE    p.created_at BETWEEN (CURRENT_DATE - INTERVAL '1 week') AND CURRENT_DATE and characteristics_key like 'memory'
                       """;
        String countSql = "SELECT COUNT(*) FROM (" + sql + ") as count_query";
        int count = jdbcTemplate.queryForObject(countSql, Integer.class);
        int totalPage = (int) Math.ceil((double) count / pageSize);
        int offset = (page - 1) * pageSize;
        sql = String.format(sql + "LIMIT %s OFFSET %s", pageSize, offset);
        List<ProductsResponse> products = jdbcTemplate.query(sql, (resultSet, i) -> new ProductsResponse(
                resultSet.getString("image"),
                resultSet.getInt("quantity"),
                resultSet.getString("product_info"),
                resultSet.getDouble("rating"),
                resultSet.getBigDecimal("price"),
                resultSet.getInt("discount")
        ));
        return PaginationResponse.<ProductsResponse>builder()
                .elements(products)
                .currentPage(page)
                .totalPages(totalPage)
                .build();
    }

    @Override
    public PaginationResponse<ProductsResponse> getRecommendedProducts(int page, int pageSize) {
        String sql = """
                       SELECT (select i.images from sub_product_images i where i.sub_product_id = sp.id limit 1) as image, sp.quantity as quantity, CONCAT(c.name, ' ', p.brand_id, ' ', p.name, ' ',characteristics,' ', sp.colour) as product_info, p.rating as rating, sp.price as price,
                       coalesce(CAST(sp.price - ((sp.price * d.percent) / 100) AS INTEGER),0) as discount
                FROM products p
                    JOIN sub_products sp ON p.id = sp.product_id
                    LEFT JOIN discounts d ON p.discount_id = d.id
                    JOIN sub_categories sc ON p.sub_category_id = sc.id
                    JOIN categories c ON sc.category_id = c.id
                    JOIN sub_product_characteristics spc ON sp.id = spc.sub_product_id
                WHERE    p.rating > 4  and characteristics_key like 'memory'
                       """;
        String countSql = "SELECT COUNT(*) FROM (" + sql + ") as count_query";
        int count = jdbcTemplate.queryForObject(countSql, Integer.class);
        int totalPage = (int) Math.ceil((double) count / pageSize);

        int offset = (page - 1) * pageSize;
        sql = String.format(sql + "LIMIT %s OFFSET %s", pageSize, offset);
        List<ProductsResponse> products = jdbcTemplate.query(sql, (resultSet, i) -> new ProductsResponse(
                resultSet.getString("image"),
                resultSet.getInt("quantity"),
                resultSet.getString("product_info"),
                resultSet.getDouble("rating"),
                resultSet.getBigDecimal("price"),
                resultSet.getInt("discount")
        ));
        return PaginationResponse.<ProductsResponse>builder()
                .elements(products)
                .currentPage(page)
                .totalPages(totalPage)
                .build();
    }

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
            case "в продаже" -> "JOIN orders_sub_products o ON o.sub_products_id = s.id";
            case "в избранном" -> "JOIN users_favorites f ON f.favorites_id = s.id";
            case "в корзине" -> "JOIN user_basket b ON b.basket_key = s.id";
            default -> "";
        };


        String dateClause = "";
        if (from != null && before != null) {
            if (from.isAfter(before)) {
                throw new BadRequestException("The from date must be earlier than the date before");
            } else if (from.isAfter(LocalDate.now()) || before.isAfter(LocalDate.now())) {
                throw new BadRequestException("The date must be in the past tense");
            }
            dateClause = String.format("AND p.created_at BETWEEN '%s' AND '%s'", from, before);
        } else if (from != null) {
            if (from.isAfter(LocalDate.now())) {
                throw new BadRequestException("The date must be in the past tense");
            }
            dateClause = "AND p.created_at >= '%s'".formatted(from);
        } else if (before != null) {
            if (before.isAfter(LocalDate.now())) {
                throw new BadRequestException("The date must be in the past tense");
            }
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
                    p.name iLIKE ? OR CASt(s.item_number AS TEXT) iLIKE ?
                    OR p.description iLIKE ? OR CAST(s.price AS TEXT) iLIKE ?
                    OR ch.characteristics iLIKE ?
                    """;
        }
        String joinType = "LEFT";
        String orderBy = "";
        if (sortBy != null) {
            switch (sortBy) {
                case "Новинки" -> orderBy = "AND p.created_at >= CURRENT_DATE - interval '7 day'";
                case "Все акции" -> joinType = "";
                case "До 50%" -> orderBy = "AND d.percent < 50";
                case "Свыше 50%" -> orderBy = "AND d.percent >= 50";
                case "Рекомендуемые" -> orderBy = "AND p.rating >= 4";
                case "По увеличению цены" -> orderBy = "ORDER BY s.price";
                case "По уменьшению цены" -> orderBy = "ORDER BY s.price DESC";
            }
        }

        sql = String.format(sql, sqlStatus, joinType, keywordCondition, dateClause, orderBy);

        String countSql = "SELECT COUNT(*) FROM (" + sql + ") AS count_query";
        int count = jdbcTemplate.queryForObject(countSql, params.toArray(), Integer.class);
        int totalPage = (int) Math.ceil((double) count / pageSize);

        sql = sql + " LIMIT ? OFFSET ?";
        int offset = (page - 1) * pageSize;
        params.add(pageSize);
        params.add(offset);
        List<ProductAdminResponse> products = jdbcTemplate.query(sql, params.toArray(), (resultSet, i) -> new ProductAdminResponse(
                resultSet.getLong("id"),
                resultSet.getString("image"),
                resultSet.getInt("item_number"),
                resultSet.getString("name"),
                LocalDate.parse(resultSet.getString("created_at")),
                resultSet.getInt("quantity"),
                resultSet.getBigDecimal("price"),
                resultSet.getInt("percent"),
                resultSet.getBigDecimal("total_price")
        ));
        return PaginationResponse.<ProductAdminResponse>builder()
                .foundProducts(count)
                .elements(products)
                .currentPage(page)
                .totalPages(totalPage)
                .build();
    }

    @Override
    public ProductUserResponse getProductById(ProductUserRequest productUserRequest) {
        String sql = """
                select b.logo as logo,
                       p.name as product_name,
                       sp.quantity as quantity,
                       sp.item_number as item_number,
                       p.rating as rating,
                       (select count(r.id)
                        from reviews r
                                 join products p on p.id = r.product_id
                        where p.id = ? ) as count_of_reviews,
                       sp.colour as color,
                       d.percent as percent_of_discount,
                       ((sp.price - (sp.price * d.percent / 100)) *  ?  ) as price,
                       sp.price as old_price,
                       p.date_of_issue  as date_of_issue,
                       p.description as description,
                       p.video as video_link
                from products p
                         join brands b on b.id = p.brand_id
                         join sub_products sp on p.id = sp.product_id
                         join reviews r on p.id = r.product_id
                         left join discounts d on d.id = p.discount_id
                where p.id =  ?  and sp.colour =  ?
                """;
        ProductUserResponse productUserResponse = new ProductUserResponse();
        jdbcTemplate.query(sql, (resulSet, i) -> {

                    productUserResponse.setLogo(resulSet.getString("logo"));
                    productUserResponse.setName(resulSet.getString("product_name"));
                    productUserResponse.setQuantity(resulSet.getInt("quantity"));
                    productUserResponse.setItemNumber(resulSet.getString("item_number"));
                    productUserResponse.setRating(resulSet.getDouble("rating"));
                    productUserResponse.setCountOfReviews(resulSet.getInt("count_of_reviews"));
                    productUserResponse.setColor(resulSet.getString("color"));
                    productUserResponse.setPercentOfDiscount(resulSet.getInt("percent_of_discount"));
                    productUserResponse.setPrice(resulSet.getBigDecimal("price"));
                    productUserResponse.setOldPrice(resulSet.getBigDecimal("old_price"));
                    productUserResponse.setDateOfIssue(resulSet.getDate("date_of_issue").toLocalDate());
                    productUserResponse.setDescription(resulSet.getString("description"));
                    productUserResponse.setVideo(resulSet.getString("video_link"));
                    return productUserResponse;
                }
                , productUserRequest.getProductId()
                , productUserRequest.getQuantity()
                , productUserRequest.getProductId()
                , productUserRequest.getColor()
        );
        String sqlColours= """
                select sp.colour as colours from sub_products sp where sp.product_id=?
                """;
        List<String> colours = jdbcTemplate.query(sqlColours, (resultSet, i) -> resultSet.getString("colours"), productUserRequest.getProductId());
        productUserResponse.setColours(colours);
        String sql2 = """
                 select spc.characteristics_key as characteristics_key
                 , spc.characteristics as characteristics
                 from sub_product_characteristics spc
                          join sub_products sp on sp.id = spc.sub_product_id
                          where sp.product_id = ?
                """;
        Map<String, String> characteristics = new LinkedHashMap<>();
        jdbcTemplate.query(sql2, (resultSet, i) ->
                        characteristics.put(resultSet.getString("characteristics_key")
                                , resultSet.getString("characteristics"))
                , productUserRequest.getProductId());
        productUserResponse.setCharacteristics(characteristics);
        String sql3 = """
                select spi.images as images
                from sub_product_images spi
                         join sub_products sp on sp.id = spi.sub_product_id
                         where sp.product_id = ? 
                """;
        List<String> images = jdbcTemplate.query(sql3, (resultSet, i) ->
                resultSet.getString("images"), productUserRequest.getProductId());
        productUserResponse.setImages(images);
        String sql4 = """
                select u.image as image,
                        concat(u.first_name, ' ', u.last_name) as full_name,
                        concat(r.created_at_data,' ',r.created_at_time) as created_at,
                        r.grade as grade,
                        r.commentary as commentary,
                        r.answer as answer
                from reviews r
                         join users u on u.id = r.user_id where r.product_id= ?
                         ORDER BY r.created_at_data DESC LIMIT ?            
                """;
        List<ReviewsResponse> reviewsResponses = jdbcTemplate.query(sql4, (resultSet, i) -> ReviewsResponse.builder()
                        .image(resultSet.getString("image"))
                        .fullName(resultSet.getString("full_name"))
                        .createdAt(resultSet.getString("created_at"))
                        .grade(resultSet.getInt("grade"))
                        .commentary(resultSet.getString("commentary"))
                        .answer(resultSet.getString("answer")).build(),
                productUserRequest.getProductId(),
                productUserRequest.getPage()
        );
        productUserResponse.setReviews(reviewsResponses);
        return productUserResponse;
    }
}
