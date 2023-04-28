package com.example.gadgetariumb8.db.service.impl;

import com.example.gadgetariumb8.db.dto.request.ProductRequest;
import com.example.gadgetariumb8.db.dto.request.SubProductRequest;
import com.example.gadgetariumb8.db.dto.response.CatalogProductsResponse;
import com.example.gadgetariumb8.db.dto.response.CatalogResponse;
import com.example.gadgetariumb8.db.dto.response.CompareProductResponse;
import com.example.gadgetariumb8.db.dto.response.PaginationResponse;
import com.example.gadgetariumb8.db.dto.response.ProductAdminResponse;
import com.example.gadgetariumb8.db.dto.response.ProductsResponse;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.example.gadgetariumb8.db.exception.exceptions.BadRequestException;
import com.example.gadgetariumb8.db.exception.exceptions.NotFoundException;
import com.example.gadgetariumb8.db.model.*;
import com.example.gadgetariumb8.db.repository.BrandRepository;
import com.example.gadgetariumb8.db.repository.SubCategoryRepository;
import com.example.gadgetariumb8.db.repository.SubProductRepository;
import com.example.gadgetariumb8.db.repository.UserRepository;
import com.example.gadgetariumb8.db.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final SubCategoryRepository subCategoryRepository;
    private final BrandRepository brandRepository;
    private final SubProductRepository subProductRepository;
    private final JdbcTemplate jdbcTemplate;
    private final UserRepository userRepository;

    private User getAuthenticate() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        log.info("Token has been taken!");
        return userRepository.findUserInfoByEmail(login).orElseThrow(() -> {
            log.error("User not found!");
            return new NotFoundException("User not found!");
        }).getUser();
    }

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
    public CatalogResponse findByCategoryIdAndFilter(Long categoryId, Optional<Long> subCategoryId,
                                                     String brand, String priceFrom, String priceTo,
                                                     String colour, String memory, String RAM, String watch_material,
                                                     String gender, String sortBy, int pageSize) {

        User user = getAuthenticate();
        List<SubProduct> usersFavourites = user.getFavorites();

        //Получить все подкатегории, которых ID категории равен categoryId
        List<SubCategory> subCategories = jdbcTemplate.query("SELECT sub.id,sub.name,sub.category_id FROM sub_categories sub WHERE sub.category_id=?",
                new Object[]{categoryId},
                new BeanPropertyRowMapper<>(SubCategory.class));
        List<CatalogProductsResponse> catalogProductsResponse = new ArrayList<>();
        String sql = """
                 SELECT sub.id,sub.colour,sub.price,sub.quantity,prod.name,prod.rating,prod.created_at,COALESCE(d.percent, 0) AS discount,
                 (SELECT i.images FROM sub_product_images i WHERE i.sub_product_id = sub.id LIMIT 1) AS image,
                    CASE WHEN d.percent IS NOT NULL THEN ROUND(sub.price - (sub.price * d.percent / 100))
                     ELSE sub.price END AS new_price,
                 (SELECT count(r) as reviews_count FROM products p join reviews r on p.id = r.product_id WHERE product_id=prod.id) as reviews_count,
                 (SELECT char.characteristics FROM sub_product_characteristics char LEFT JOIN sub_products sp ON char.sub_product_id = sp.id WHERE char.characteristics_key='Объем памяти' AND sub_product_id = sub.id LIMIT 1) as memory
                 FROM sub_products sub
                     JOIN products prod ON sub.product_id = prod.id
                     LEFT JOIN sub_product_characteristics spc on sub.id = spc.sub_product_id
                     %7$s JOIN discounts d ON prod.discount_id = d.id
                     %1$s
                 WHERE sub_category_id=? %2$s  %3$s  %4$s  %5$s  %6$s  %8$s %9$s %10$s
                 LIMIT ?
                """;
        sql = filteringAndSorting(sql, brand, priceFrom, priceTo, colour,
                memory, RAM, watch_material, gender, sortBy);

        Map<String, Long> quantityColours = new HashMap<>();

        // Проверка наличия подкатегории, если нет, получить продукты по категориям
        if (subCategoryId.isEmpty()) {
            for (SubCategory subCategory : subCategories) {
                List<CatalogProductsResponse> subProducts = jdbcTemplate.query(sql,
                        new Object[]{subCategory.getId(), pageSize},
                        (resultSet, i) -> rowMapper(resultSet));
                catalogProductsResponse.addAll(subProducts);
            }

            //Ставить лайки всем полученным товарам, которые были в избранном у пользователя,
            // и подсчитывать товары определенного цвета
            return getCatalogResponse(usersFavourites, catalogProductsResponse, quantityColours);
        }

        SubCategory subCategory = subCategories
                .stream()
                .filter(subCat -> Objects.equals(subCat.getId(), subCategoryId.get()))
                .findAny()
                .orElseThrow(() -> new NotFoundException("Products with this category and subcategory ID not found!"));

        catalogProductsResponse.addAll(jdbcTemplate.query(sql,
                new Object[]{subCategory.getId(), pageSize},
                (resultSet, i) -> rowMapper(resultSet)));

        // Ставить лайки всем полученным товарам, которые были в избранном у пользователя, и подсчитывать товары определенного цвета
        return getCatalogResponse(usersFavourites, catalogProductsResponse, quantityColours);
    }

    private CatalogResponse getCatalogResponse(List<SubProduct> usersFavourites, List<CatalogProductsResponse> catalogProductsResponse, Map<String, Long> quantityColours) {
        for (CatalogProductsResponse productsResponse : catalogProductsResponse) {
            productsResponse.setIsLiked(usersFavourites.stream()
                    .map(SubProduct::getId)
                    .anyMatch(productsResponse.getSub_product_id()::equals));
        }
        quantityColours.put("Black", catalogProductsResponse.stream().filter(item -> Objects.equals(item.getColour(), "Black")).count());
        quantityColours.put("Blue", catalogProductsResponse.stream().filter(item -> Objects.equals(item.getColour(), "Blue")).count());
        quantityColours.put("White", catalogProductsResponse.stream().filter(item -> Objects.equals(item.getColour(), "White")).count());
        quantityColours.put("Red", catalogProductsResponse.stream().filter(item -> Objects.equals(item.getColour(), "Red")).count());
        quantityColours.put("Gold", catalogProductsResponse.stream().filter(item -> Objects.equals(item.getColour(), "Gold")).count());
        quantityColours.put("Graphite", catalogProductsResponse.stream().filter(item -> Objects.equals(item.getColour(), "Graphite")).count());
        quantityColours.put("Green", catalogProductsResponse.stream().filter(item -> Objects.equals(item.getColour(), "Green")).count());
        quantityColours.put("Rose Gold", catalogProductsResponse.stream().filter(item -> Objects.equals(item.getColour(), "Rose Gold")).count());
        quantityColours.put("Silver", catalogProductsResponse.stream().filter(item -> Objects.equals(item.getColour(), "Silver")).count());
        quantityColours.put("Purple", catalogProductsResponse.stream().filter(item -> Objects.equals(item.getColour(), "Purple")).count());
        return CatalogResponse.builder()
                .productsResponses(catalogProductsResponse)
                .colourQuantity(quantityColours)
                .build();
    }

    private String filteringAndSorting(String sql, String brand, String priceFrom, String priceTo, String colour, String memory, String RAM, String watch_material, String gender, String sortBy) {
        // Фильтрация по бренду
        String joiningForFilterByBrand = "";
        String conditionForFilterByBrand = "";
        if (brand != null) {
            joiningForFilterByBrand = "LEFT JOIN brands b on prod.brand_id = b.id";
            conditionForFilterByBrand = String.format("AND b.name = '%s'", brand);
        }
        // Фильтрация по цене
        String filterByPrice = String.format("AND sub.price BETWEEN %s AND %s", priceFrom, priceTo);

        //Фильтрация по цвету
        String filterByColour = "";
        if (colour != null) {
            filterByColour = String.format("AND sub.colour='%s'", colour);
        }

        // Фильтрация по характеристикам
        String conditionForFilterByMemory = "";
        String conditionForFilterByRAM = "";
        String conditionForFilterByMaterial = "";
        String conditionForFilterByGender = "";
        if (memory != null) {
            conditionForFilterByMemory = String.format("AND spc.characteristics='%sGB' AND spc.characteristics_key='Объем памяти'", memory);    // maybe changed
        }
        if (RAM != null) {
            conditionForFilterByRAM = String.format("AND spc.characteristics='%sGB' AND spc.characteristics_key='Оперативная память'", RAM);                  // maybe changed
        }
        if (watch_material != null) {
            conditionForFilterByMaterial = String.format("AND spc.characteristics='%s' AND spc.characteristics_key='Материал корпуса'", watch_material);
        }
        if (gender != null) {
            conditionForFilterByGender = String.format("AND spc.characteristics='%s' AND spc.characteristics_key='Пол'", gender);
        }

        // Сортировка по некоторым критериям
        String orderBy = "";
        String joinTypeOfDiscount = "LEFT";
        if (sortBy != null) {
            switch (sortBy) {
                case "Новинки" ->
                        orderBy = "AND prod.created_at >= NOW() - INTERVAL '7 days' ORDER BY prod.created_at DESC";
                case "Все акции" -> joinTypeOfDiscount = "";
                case "До 50%" -> orderBy = "AND d.percent < 50";
                case "Свыше 50%" -> orderBy = "AND d.percent >= 50";
                case "Рекомендуемые" -> orderBy = "AND prod.rating >= 4";
                case "По увеличению цены" -> orderBy = "ORDER BY sub.price";
                case "По уменьшению цены" -> orderBy = "ORDER BY sub.price DESC";
            }
        }

        sql = String.format(sql, joiningForFilterByBrand, conditionForFilterByBrand, filterByPrice,
                filterByColour, conditionForFilterByMemory, conditionForFilterByRAM, joinTypeOfDiscount,
                conditionForFilterByMaterial, conditionForFilterByGender, orderBy);
        return sql;
    }

    public CatalogProductsResponse rowMapper(ResultSet resultSet) throws SQLException {
        return CatalogProductsResponse.builder()
                .sub_product_id(resultSet.getLong("id"))
                .price(resultSet.getBigDecimal("price"))
                .quantity(resultSet.getInt("quantity"))
                .fullname(resultSet.getString("name") + " " + resultSet.getObject("memory") + " " + resultSet.getString("colour"))
                .rating(resultSet.getDouble("rating"))
                .discount(resultSet.getInt("discount"))
                .image(resultSet.getString("image"))
                .colour(resultSet.getString("colour"))
                .new_price(resultSet.getBigDecimal("new_price"))
                .reviews_count(resultSet.getInt("reviews_count"))
                .isNew(resultSet.getDate("created_at").toLocalDate().isAfter(LocalDate.now().minusDays(7)))
                .build();
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
    public List<CompareProductResponse> compare() {
        String sql = """
                SELECT (SELECT sci FROM sub_product_images sci where sci.sub_product_id = sp.id LIMIT 1) as image,(p.name) as name
                     ,p.description as description,sp.price as price ,b.name as brand_name,
                      (SELECT spc.characteristics from sub_product_characteristics spc where spc.characteristics_key='screen' and spc.sub_product_id = sp.id) as screen,
                      sp.colour as color,
                      (SELECT spc.characteristics from sub_product_characteristics spc where spc.characteristics_key='operatingSystem' and spc.sub_product_id = sp.id) as operatingSystem,
                      (SELECT spc.characteristics from sub_product_characteristics spc where spc.characteristics_key='memory' and spc.sub_product_id = sp.id) as memory,
                      (SELECT spc.characteristics from sub_product_characteristics spc where spc.characteristics_key='weight' and spc.sub_product_id = sp.id) as weight,
                      (SELECT spc.characteristics from sub_product_characteristics spc where spc.characteristics_key='simCard' and spc.sub_product_id = sp.id) as simCard
                              
                FROM products p JOIN sub_products sp on p.id = sp.product_id
                    JOIN users_comparisons uc on uc.comparisons_id = sp.id
                    JOIN users u on uc.user_id = u.id JOIN brands b on p.brand_id = b.id where u.id = ?
                 """;
        return jdbcTemplate.query(sql, (resultSet, i) ->
                new CompareProductResponse(
                        resultSet.getString("image"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getBigDecimal("price"),
                        resultSet.getString("brand_name"),
                        resultSet.getString("screen"),
                        resultSet.getString("color"),
                        resultSet.getString("operatingSystem"),
                        resultSet.getString("memory"),
                        resultSet.getString("weight"),
                        resultSet.getString("simCard")
                ), getAuthenticate().getId()
        );
    }

    @Override
    public PaginationResponse<ProductsResponse>  getNewProducts(int page, int pageSize) {
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
    public PaginationResponse<ProductsResponse>  getRecommendedProducts(int page, int pageSize) {
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
}