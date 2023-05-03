package com.example.gadgetariumb8.db.service.impl;

import com.example.gadgetariumb8.db.dto.request.ProductRequest;
import com.example.gadgetariumb8.db.dto.request.ProductUserRequest;
import com.example.gadgetariumb8.db.dto.request.SubProductRequest;
import com.example.gadgetariumb8.db.dto.response.*;
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
                .orElseThrow(() -> new NotFoundException("Sub category with id:" + productRequest.subCategoryId() + " not found!"));

        Brand brand = brandRepository.findById(productRequest.brandId())
                .orElseThrow(() -> new NotFoundException("Brand with id:" + productRequest.brandId() + " not found!"));
        Product product = new Product();
        product.setSubCategory(subCategory);
        product.setBrand(brand);
        product.setGuarantee(productRequest.guarantee());
        product.setName(productRequest.name());
        product.setDateOfIssue(productRequest.dateOfIssue());
        product.setCreatedAt(LocalDate.now());
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
                SELECT sp.id as subProductId,
                (select i.images from sub_product_images i where i.sub_product_id = sp.id limit 1) as image,
                sp.quantity as quantity, CONCAT(c.name, ' ', p.brand_id, ' ', p.name, ' ', spc.characteristics,' ',
                  sp.colour) as product_info, p.rating as rating, sp.price as price,
                 CAST(sp.price - ((sp.price * d.percent) / 100) AS INTEGER) as discount
                FROM products p
                    JOIN sub_products sp ON p.id = sp.product_id
                    JOIN discounts d ON p.discount_id = d.id
                    JOIN sub_categories sc ON p.sub_category_id = sc.id
                    JOIN categories c ON sc.category_id = c.id
                    LEFT JOIN sub_product_characteristics spc ON sp.id = spc.sub_product_id
                    WHERE spc.characteristics_key like 'память'
                """;

        String countSql = "SELECT COUNT(*) FROM (" + sql + ") as count_query";
        int count = jdbcTemplate.queryForObject(countSql, Integer.class);
        int totalPage = (int) Math.ceil((double) count / pageSize);

        int offset = (page - 1) * pageSize;
        sql = String.format(sql + "LIMIT %s OFFSET %s", pageSize, offset);
        List<ProductsResponse> products = jdbcTemplate.query(sql, (resultSet, i) -> new ProductsResponse(
                resultSet.getLong("subProductId"),
                resultSet.getString("image"),
                resultSet.getInt("quantity"),
                resultSet.getString("product_info"),
                resultSet.getDouble("rating"),
                resultSet.getBigDecimal("price"),
                resultSet.getInt("discount")
        ));

        return PaginationResponse.<ProductsResponse>builder()
                .foundProducts(count)
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
    public PaginationResponse<ProductsResponse> getNewProducts(int page, int pageSize) {
        String sql = """
                SELECT sp.id as subProductId, (select i.images from sub_product_images i where i.sub_product_id = sp.id limit 1) as image,
                 sp.quantity as quantity, CONCAT(c.name, ' ', p.brand_id, ' ', p.name, ' ', spc.characteristics,' ', sp.colour) as product_info,
                 p.rating as rating, sp.price as price,
                 coalesce(CAST(sp.price - ((sp.price * d.percent) / 100) AS INTEGER),0) as discount
                FROM products p
                    JOIN sub_products sp ON p.id = sp.product_id
                    LEFT JOIN discounts d ON p.discount_id = d.id
                    JOIN sub_categories sc ON p.sub_category_id = sc.id
                    JOIN categories c ON sc.category_id = c.id
                    JOIN sub_product_characteristics spc ON sp.id = spc.sub_product_id
                WHERE p.created_at BETWEEN (CURRENT_DATE - INTERVAL '1 week') AND CURRENT_DATE AND spc.characteristics_key like 'память'
                                
                """;
        String countSql = "SELECT COUNT(*) FROM (" + sql + ") as count_query";
        int count = jdbcTemplate.queryForObject(countSql, Integer.class);
        int totalPage = (int) Math.ceil((double) count / pageSize);

        int offset = (page - 1) * pageSize;
        sql = String.format(sql + "LIMIT %s OFFSET %s", pageSize, offset);
        List<ProductsResponse> products = jdbcTemplate.query(sql, (resultSet, i) -> new ProductsResponse(
                resultSet.getLong("subProductId"),
                resultSet.getString("image"),
                resultSet.getInt("quantity"),
                resultSet.getString("product_info"),
                resultSet.getDouble("rating"),
                resultSet.getBigDecimal("price"),
                resultSet.getInt("discount")
        ));
        return PaginationResponse.<ProductsResponse>builder()
                .foundProducts(count)
                .elements(products)
                .currentPage(page)
                .totalPages(totalPage)
                .build();
    }

    @Override
    public PaginationResponse<ProductsResponse> getRecommendedProducts(int page, int pageSize) {
        String sql = """
                SELECT sp.id AS subProductId, (select i.images from sub_product_images i where i.sub_product_id = sp.id limit 1) as image,
                sp.quantity as quantity,
                CONCAT(c.name, ' ', sc.name, ' ', p.name, ' ', spc.characteristics,' ', sp.colour) as product_info,
                p.rating as rating, sp.price as price,
                coalesce(CAST(sp.price - ((sp.price * d.percent) / 100) AS INTEGER),0) as discount
                FROM products p
                    JOIN sub_products sp ON p.id = sp.product_id
                    LEFT JOIN discounts d ON p.discount_id = d.id
                    JOIN sub_categories sc ON p.sub_category_id = sc.id
                    JOIN categories c ON sc.category_id = c.id
                    JOIN sub_product_characteristics spc ON sp.id = spc.sub_product_id
                WHERE p.rating > 4 AND spc.characteristics_key  like 'память'
                """;
        String countSql = "SELECT COUNT(*) FROM (" + sql + ") as count_query";
        int count = jdbcTemplate.queryForObject(countSql, Integer.class);
        int totalPage = (int) Math.ceil((double) count / pageSize);

        int offset = (page - 1) * pageSize;
        sql = String.format(sql + "LIMIT %s OFFSET %s", pageSize, offset);
        List<ProductsResponse> products = jdbcTemplate.query(sql, (resultSet, i) -> new ProductsResponse(
                resultSet.getLong("subProductId"),
                resultSet.getString("image"),
                resultSet.getInt("quantity"),
                resultSet.getString("product_info"),
                resultSet.getDouble("rating"),
                resultSet.getBigDecimal("price"),
                resultSet.getInt("discount")
        ));
        return PaginationResponse.<ProductsResponse>builder()
                .foundProducts(count)
                .elements(products)
                .currentPage(page)
                .totalPages(totalPage)
                .build();
    }

    @Override
    public PaginationResponse<ProductAdminResponse> getAll(String keyWord, String status, LocalDate from, LocalDate before, String sortBy, int page, int pageSize) {
        String sql = """
                SELECT DISTINCT sp.id AS subProductId, (
                    SELECT i.images FROM sub_product_images i WHERE i.sub_product_id = sp.id LIMIT 1
                ) AS image, sp.item_number, CONCAT(sc.name, ' ', p.name, ' ', spc.characteristics,' ', sp.colour) AS name, p.created_at, sp.quantity, sp.price,
                COALESCE(d.percent, 0) AS percent,
                CASE WHEN d.percent IS NOT NULL THEN ROUND(sp.price - (sp.price * d.percent / 100))
                ELSE sp.price END AS total_price
                FROM sub_products sp
                JOIN products p ON p.id = sp.product_id
                JOIN sub_categories sc ON sc.id = p.sub_category_id
                %s
                LEFT JOIN sub_product_characteristics spc ON spc.sub_product_id = sp.id
                %s JOIN discounts d ON d.id = p.discount_id
                WHERE %s %s AND spc.characteristics_key  like 'память'
                %s
                """;
        String sqlStatus = switch (status) {
            case "в продаже" -> "JOIN orders_sub_products o ON o.sub_products_id = sp.id";
            case "в избранном" -> "JOIN users_favorites f ON f.favorites_id = sp.id";
            case "в корзине" -> "JOIN user_basket b ON b.basket_key = sp.id";
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
                    p.name iLIKE ? OR CAST(sp.item_number AS TEXT) iLIKE ?
                    OR p.description iLIKE ? OR CAST(sp.price AS TEXT) iLIKE ?
                    OR spc.characteristics iLIKE ?
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
                case "По увеличению цены" -> orderBy = "ORDER BY total_price";
                case "По уменьшению цены" -> orderBy = "ORDER BY total_price DESC";
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
                resultSet.getLong("subProductId"),
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
    public CatalogResponse findByCategoryIdAndFilter(Long categoryId, Optional<Long> subCategoryId,
                                                     String[] brand, String priceFrom, String priceTo,
                                                     String[] colour, String[] memory, String[] RAM, String[] watch_material,
                                                     String gender, String sortBy, int pageSize) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        User user = userRepository.findUserByEmail(login).orElseThrow(() -> new NotFoundException("User not found!"));
        List<SubProduct> usersFavourites = user.getFavorites();

        List<SubCategory> subCategories = jdbcTemplate.query("SELECT sub.id,sub.name,sub.category_id FROM sub_categories sub WHERE sub.category_id=?",
                new Object[]{categoryId},
                new BeanPropertyRowMapper<>(SubCategory.class));
        List<CatalogProductsResponse> catalogProductsResponse = new ArrayList<>();
        String sql = """
                 SELECT sub.id,sub.colour,sub.price,sub.quantity,prod.name,prod.rating,prod.created_at,COALESCE(dis.percent, 0) AS discount,
                        (SELECT i.images FROM sub_product_images i WHERE i.sub_product_id = sub.id LIMIT 1) AS image,
                        CASE WHEN dis.percent IS NOT NULL THEN ROUND(sub.price - (sub.price * dis.percent / 100))
                             ELSE sub.price END AS new_price,
                        (SELECT count(r) as reviews_count FROM products p join reviews r on p.id = r.product_id WHERE product_id=prod.id) as reviews_count,
                        (SELECT char.characteristics FROM sub_product_characteristics char LEFT JOIN sub_products sp ON char.sub_product_id = sp.id WHERE char.characteristics_key='память' AND sub_product_id = sub.id LIMIT 1) as memory
                 FROM sub_products sub
                        JOIN products prod ON sub.product_id = prod.id
                        LEFT JOIN sub_product_characteristics spc on sub.id = spc.sub_product_id
                        %7$s JOIN discounts dis ON prod.discount_id = dis.id
                        %1$s
                 WHERE sub_category_id=? %2$s  %3$s  %4$s  %5$s  %6$s  %8$s %9$s %10$s
                 LIMIT ?
                """;
        sql = filteringAndSorting(sql, brand, priceFrom, priceTo, colour,
                memory, RAM, watch_material, gender, sortBy);

        Map<String, Long> quantityColours = new HashMap<>();

        if (subCategoryId.isEmpty()) {
            for (SubCategory subCategory : subCategories) {
                List<CatalogProductsResponse> subProducts = jdbcTemplate.query(sql,
                        new Object[]{subCategory.getId(), pageSize},
                        (resultSet, i) -> rowMapper(resultSet));
                catalogProductsResponse.addAll(subProducts);
            }

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

        return getCatalogResponse(usersFavourites, catalogProductsResponse, quantityColours);
    }

    private CatalogResponse getCatalogResponse(List<SubProduct> usersFavourites,
                                               List<CatalogProductsResponse> catalogProductsResponse,
                                               Map<String, Long> quantityColours) {
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
                .productQuantity(catalogProductsResponse.size())
                .build();
    }

    private String filteringAndSorting(String sql, String[] brand, String priceFrom, String priceTo, String[] colour,
                                       String[] memory, String[] RAM, String[] watch_material, String gender, String sortBy) {
        String joiningForFilterByBrand = "";
        String conditionForFilterByBrand = "";
        if (brand != null) {
            String brands = stringify(brand);
            joiningForFilterByBrand = "LEFT JOIN brands b on prod.brand_id = b.id";
            conditionForFilterByBrand = String.format("AND b.name IN (%s)", brands);
        }
        String filterByPrice = String.format("AND sub.price BETWEEN %s AND %s", priceFrom, priceTo);

        String filterByColour = "";
        if (colour != null) {
            String colours = stringify(colour);
            filterByColour = String.format("AND sub.colour IN(%s)", colours);
        }
        String conditionForFilterByMemory = "";
        String conditionForFilterByRAM = "";
        String conditionForFilterByMaterial = "";
        String conditionForFilterByGender = "";
        if (memory != null) {
            String memories = stringify(memory);
            conditionForFilterByMemory = String.format("AND spc.characteristics IN (%s) AND spc.characteristics_key='память'", memories);
        }
        if (RAM != null) {
            String rams = stringify(RAM);
            conditionForFilterByRAM = String.format("AND spc.characteristics IN (%s) AND spc.characteristics_key='Оперативная память'", rams);
        }
        if (watch_material != null) {
            String watch_materials = stringify(watch_material);
            conditionForFilterByMaterial = String.format("AND spc.characteristics IN (%s) AND spc.characteristics_key='Материал корпуса'", watch_materials);
        }
        if (gender != null) {
            conditionForFilterByGender = String.format("AND spc.characteristics='%s' AND spc.characteristics_key='Пол'", gender);
        }

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

    private String stringify(String[] elements) {
        StringBuilder elementsToString = new StringBuilder();
        for (String b : elements) {
            elementsToString.append("'").append(b).append("',");
        }
        elementsToString.deleteCharAt(elementsToString.length() - 1);
        return elementsToString.toString();
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
    public ProductUserResponse getProductById(ProductUserRequest productUserRequest) {
        String sql = """
                select sp.id as sub_product_id
                       b.logo as logo,
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
                    productUserResponse.setSubProductId(resulSet.getLong("sub_product_id"));
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