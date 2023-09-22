package com.example.gadgetariumb8.db.service.impl;

import com.example.gadgetariumb8.db.dto.request.ProductRequest;
import com.example.gadgetariumb8.db.dto.request.ProductUpdateRequest;
import com.example.gadgetariumb8.db.dto.request.SubProductRequest;
import com.example.gadgetariumb8.db.dto.response.*;
import com.example.gadgetariumb8.db.dto.response.catalog.CatalogProductsResponse;
import com.example.gadgetariumb8.db.dto.response.catalog.CatalogResponse;
import com.example.gadgetariumb8.db.dto.response.catalog.ColourResponse;
import com.example.gadgetariumb8.db.exception.exceptions.BadRequestException;
import com.example.gadgetariumb8.db.exception.exceptions.NotFoundException;
import com.example.gadgetariumb8.db.model.*;
import com.example.gadgetariumb8.db.model.enums.Status;
import com.example.gadgetariumb8.db.repository.*;
import com.example.gadgetariumb8.db.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final BrandRepository brandRepository;
    private final SubProductRepository subProductRepository;
    private final JdbcTemplate jdbcTemplate;
    private final UserRepository userRepository;

    @Override
    public SimpleResponse saveProduct(ProductRequest productRequest) {
        SubCategory subCategory = subCategoryRepository.findById(productRequest.subCategoryId())
                .orElseThrow(() -> {
                    log.error("Подкатегория с идентификатором: " + productRequest.subCategoryId() + " не найдена!");
                    throw new NotFoundException("Подкатегория с идентификатором: " + productRequest.subCategoryId() + " не найдена!");
                });

        Brand brand = brandRepository.findById(productRequest.brandId())
                .orElseThrow(() -> {
                    log.error("Бренд с идентификатором: " + productRequest.brandId() + " не найден!");
                    throw new NotFoundException("Бренд с идентификатором: " + productRequest.brandId() + " не найден!");
                });
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

            SecureRandom random = new SecureRandom();
            int randomNumber = random.nextInt(1000000, 9999999);

            SubProduct subProduct = new SubProduct();
            subProduct.addCharacteristics(s.characteristics());
            subProduct.setColour(s.colour());
            subProduct.setPrice(s.price());
            subProduct.setQuantity(s.quantity());
            subProduct.setImages(s.images());
            subProduct.setProduct(product);
            subProduct.setItemNumber(randomNumber);
            product.addSubProduct(subProduct);
            subProductRepository.save(subProduct);
        }
        log.info("Успешно сохранено!!");
        return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("Успешно сохранено!!").build();
    }

    @Override
    public PaginationResponse<ProductsResponse> getAllDiscountProducts(int page, int pageSize) {
        log.info("Получение всех товаров со скидкой!");
        String sql = """
                SELECT sp.id as subProductId,
                  (select i.images from sub_product_images i where i.sub_product_id = sp.id limit 1) as image,
                  sp.quantity as quantity, CONCAT(c.name, ' ', sc.name, ' ', p.name, ' ', spc.characteristics,' ',
                  sp.colour) as product_info, p.rating as rating, sp.price as price,
                  d.percent as discount,
                  p.created_at as createdAt,
                  count(r) as countOfReviews,
                  CASE WHEN uf IS NOT NULL THEN true ELSE false END as isInFavorites,
                  CASE WHEN uc IS NOT NULL THEN true ELSE false END as isInComparisons
                FROM products p
                    JOIN sub_products sp ON p.id = sp.product_id
                    JOIN discounts d ON sp.discount_id = d.id
                    JOIN sub_categories sc ON p.sub_category_id = sc.id
                    JOIN categories c ON sc.category_id = c.id
                    LEFT JOIN sub_product_characteristics spc ON sp.id = spc.sub_product_id
                    LEFT JOIN reviews r ON p.id = r.product_id
                    LEFT JOIN users_favorites uf ON uf.favorites_id = sp.id AND uf.user_id = ?
                    LEFT JOIN users_comparisons uc ON uc.comparisons_id = sp.id AND uc.user_id = ?
                    WHERE spc.characteristics_key like 'память'
                    GROUP BY subProductId, image, quantity, product_info, rating, price, discount, createdAt, isInFavorites, isInComparisons
                """;

        String countSql = "SELECT COUNT(*) FROM (" + sql + ") as count_query";
        int count = jdbcTemplate.queryForObject(countSql, Integer.class, getAuthenticate().getId(), getAuthenticate().getId());
        int totalPage = (int) Math.ceil((double) count / pageSize);

        int offset = (page - 1) * pageSize;
        sql = String.format(sql + "LIMIT %s OFFSET %s", pageSize, offset);
        List<ProductsResponse> products = jdbcTemplate.query(sql, (resultSet, i) -> new ProductsResponse(
                resultSet.getLong("subProductId"),
                resultSet.getString("image"),
                resultSet.getInt("quantity"),
                resultSet.getString("product_info"),
                resultSet.getDouble("rating"),
                resultSet.getInt("countOfReviews"),
                resultSet.getBigDecimal("price"),
                resultSet.getInt("discount"),
                resultSet.getDate("createdAt").toLocalDate(),
                resultSet.getBoolean("isInFavorites"),
                resultSet.getBoolean("isInComparisons")
        ), getAuthenticate().getId(), getAuthenticate().getId());
        log.info("Продукция успешно приобретена!");
        return PaginationResponse.<ProductsResponse>builder()
                .countOfElements(count)
                .elements(products)
                .currentPage(page)
                .totalPages(totalPage)
                .build();
    }

    @Override
    public PaginationResponse<ProductsResponse> getNewProducts(int page, int pageSize) {
        log.info("Получение всех новых продуктов!");
        String sql = """
                SELECT sp.id as subProductId, (select i.images from sub_product_images i where i.sub_product_id = sp.id limit 1) as image,
                 sp.quantity as quantity, CONCAT(c.name, ' ', sc.name, ' ', p.name, ' ', spc.characteristics,' ', sp.colour) as product_info,
                 p.rating as rating, sp.price as price,
                  d.percent as discount,
                  p.created_at as createdAt,
                  count(r) as countOfReviews,
                  CASE WHEN uf IS NOT NULL THEN true ELSE false END as isInFavorites,
                  CASE WHEN uc IS NOT NULL THEN true ELSE false END as isInComparisons
                FROM products p
                    JOIN sub_products sp ON p.id = sp.product_id
                    LEFT JOIN discounts d ON sp.discount_id = d.id
                    JOIN sub_categories sc ON p.sub_category_id = sc.id
                    JOIN categories c ON sc.category_id = c.id
                    JOIN sub_product_characteristics spc ON sp.id = spc.sub_product_id
                    LEFT JOIN reviews r ON p.id = r.product_id
                    LEFT JOIN users_favorites uf ON uf.favorites_id = sp.id AND uf.user_id = ?
                    LEFT JOIN users_comparisons uc ON uc.comparisons_id = sp.id AND uc.user_id = ?
                WHERE p.created_at BETWEEN (CURRENT_DATE - INTERVAL '1 week') AND CURRENT_DATE AND spc.characteristics_key like 'память'
                GROUP BY subProductId, image, quantity, product_info, rating, price, discount, createdAt, isInFavorites, isInComparisons
                """;
        String countSql = "SELECT COUNT(*) FROM (" + sql + ") as count_query";
        int count = jdbcTemplate.queryForObject(countSql, Integer.class, getAuthenticate().getId(), getAuthenticate().getId());
        int totalPage = (int) Math.ceil((double) count / pageSize);

        int offset = (page - 1) * pageSize;
        sql = String.format(sql + "LIMIT %s OFFSET %s", pageSize, offset);
        List<ProductsResponse> products = jdbcTemplate.query(sql, (resultSet, i) -> new ProductsResponse(
                resultSet.getLong("subProductId"),
                resultSet.getString("image"),
                resultSet.getInt("quantity"),
                resultSet.getString("product_info"),
                resultSet.getDouble("rating"),
                resultSet.getInt("countOfReviews"),
                resultSet.getBigDecimal("price"),
                resultSet.getInt("discount"),
                resultSet.getDate("createdAt").toLocalDate(),
                resultSet.getBoolean("isInFavorites"),
                resultSet.getBoolean("isInComparisons")
        ), getAuthenticate().getId(), getAuthenticate().getId());
        log.info("Продукты успешно получен!");
        return PaginationResponse.<ProductsResponse>builder()
                .countOfElements(count)
                .elements(products)
                .currentPage(page)
                .totalPages(totalPage)
                .build();
    }

    @Override
    public PaginationResponse<ProductsResponse> getRecommendedProducts(int page, int pageSize) {
        log.info("Получение всех рекомендованных продуктов!");
        String sql = """
                SELECT sp.id AS subProductId, (select i.images from sub_product_images i where i.sub_product_id = sp.id limit 1) as image,
                sp.quantity as quantity,
                CONCAT(c.name, ' ', sc.name, ' ', p.name, ' ', spc.characteristics,' ', sp.colour) as product_info,
                p.rating as rating, sp.price as price,
                d.percent as discount,
                p.created_at as createdAt,
                count(r) as countOfReviews,
                CASE WHEN uf IS NOT NULL THEN true ELSE false END as isInFavorites,
                CASE WHEN uc IS NOT NULL THEN true ELSE false END as isInComparisons
                FROM products p
                    JOIN sub_products sp ON p.id = sp.product_id
                    LEFT JOIN discounts d ON sp.discount_id = d.id
                    JOIN sub_categories sc ON p.sub_category_id = sc.id
                    JOIN categories c ON sc.category_id = c.id
                    JOIN sub_product_characteristics spc ON sp.id = spc.sub_product_id
                    LEFT JOIN reviews r ON p.id = r.product_id
                    LEFT JOIN users_favorites uf ON uf.favorites_id = sp.id AND uf.user_id = ?
                    LEFT JOIN users_comparisons uc ON uc.comparisons_id = sp.id AND uc.user_id = ?
                WHERE p.rating > 4 AND spc.characteristics_key  like 'память'
                GROUP BY subProductId, image, quantity, product_info, rating, price, discount, createdAt, isInFavorites, isInComparisons
                """;
        String countSql = "SELECT COUNT(*) FROM (" + sql + ") as count_query";
        int count = jdbcTemplate.queryForObject(countSql, Integer.class, getAuthenticate().getId(), getAuthenticate().getId());
        int totalPage = (int) Math.ceil((double) count / pageSize);

        int offset = (page - 1) * pageSize;
        sql = String.format(sql + "LIMIT %s OFFSET %s", pageSize, offset);
        List<ProductsResponse> products = jdbcTemplate.query(sql, (resultSet, i) -> new ProductsResponse(
                resultSet.getLong("subProductId"),
                resultSet.getString("image"),
                resultSet.getInt("quantity"),
                resultSet.getString("product_info"),
                resultSet.getDouble("rating"),
                resultSet.getInt("countOfReviews"),
                resultSet.getBigDecimal("price"),
                resultSet.getInt("discount"),
                resultSet.getDate("createdAt").toLocalDate(),
                resultSet.getBoolean("isInFavorites"),
                resultSet.getBoolean("isInComparisons")
        ), getAuthenticate().getId(), getAuthenticate().getId());
        log.info("Продукция успешно получена!");
        return PaginationResponse.<ProductsResponse>builder()
                .countOfElements(count)
                .elements(products)
                .currentPage(page)
                .totalPages(totalPage)
                .build();
    }

    @Override
    public PaginationResponse<ProductAdminResponse> getAll(String keyWord, String status, LocalDate from, LocalDate before, String sortBy, int page, int pageSize) {
        log.info("Получение всех продуктов!");
        String sql = """
                SELECT DISTINCT p.id as product_id,
                            sp.id AS subProductId, (
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
                %s JOIN discounts d ON d.id = sp.discount_id
                WHERE spc.characteristics_key  like 'память' %s %s
                %s %s
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
                log.error("The from date must be earlier than the date before");
                throw new BadRequestException("The from date must be earlier than the date before");
            } else if (from.isAfter(LocalDate.now()) || before.isAfter(LocalDate.now())) {
                log.error("The date must be in the past tense");
                throw new BadRequestException("The date must be in the past tense");
            }
            dateClause = String.format("AND p.created_at BETWEEN '%s' AND '%s'", from, before);
        } else if (from != null) {
            if (from.isAfter(LocalDate.now())) {
                log.error("The date must be in the past tense");
                throw new BadRequestException("The date must be in the past tense");
            }
            dateClause = "AND p.created_at >= '%s'".formatted(from);
        } else if (before != null) {
            if (before.isAfter(LocalDate.now())) {
                log.error("The date must be in the past tense");
                throw new BadRequestException("The date must be in the past tense");
            }
            dateClause = "AND p.created_at <= '%s'".formatted(before);
        }

        List<Object> params = new ArrayList<>();
        String keywordCondition = "AND(1=1)";
        if (keyWord != null) {
            params.add("%" + keyWord + "%");
            params.add("%" + keyWord + "%");
            params.add("%" + keyWord + "%");
            params.add("%" + keyWord + "%");
            params.add("%" + keyWord + "%");
            params.add("%" + keyWord + "%");

            keywordCondition = """
                    AND(sc.name iLIKE ? OR p.name iLIKE ? OR CAST(sp.item_number AS TEXT) iLIKE ?
                    OR p.description iLIKE ? OR CAST(sp.price AS TEXT) iLIKE ?
                    OR spc.characteristics iLIKE ?)
                    """;
        }
        String orderById = "ORDER BY sp.id DESC";
        String joinType = "LEFT";
        String orderBy = "";
        if (sortBy != null) {
            switch (sortBy) {
                case "Новинки" -> orderBy = "AND p.created_at >= CURRENT_DATE - interval '7 day'";
                case "Все акции" -> joinType = "";
                case "До 50%" -> orderBy = "AND d.percent < 50";
                case "Свыше 50%" -> orderBy = "AND d.percent >= 50";
                case "Рекомендуемые" -> orderBy = "AND p.rating >= 4";
                case "По увеличению цены" -> {
                    orderById = "";
                    orderBy = "ORDER BY total_price";
                }
                case "По уменьшению цены" -> {
                    orderById = "";
                    orderBy = "ORDER BY total_price DESC";
                }
            }
        }

        sql = String.format(sql, sqlStatus, joinType, keywordCondition, dateClause, orderBy, orderById);

        String countSql = "SELECT COUNT(*) FROM (" + sql + ") AS count_query";
        int count = jdbcTemplate.queryForObject(countSql, params.toArray(), Integer.class);
        int totalPage = (int) Math.ceil((double) count / pageSize);

        sql = sql + " LIMIT ? OFFSET ?";
        int offset = (page - 1) * pageSize;
        params.add(pageSize);
        params.add(offset);
        List<ProductAdminResponse> products = jdbcTemplate.query(sql, params.toArray(), (resultSet, i) -> new ProductAdminResponse(
                resultSet.getLong("product_id"),
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
        log.info("Продукция успешно получена!");
        return PaginationResponse.<ProductAdminResponse>builder()
                .countOfElements(count)
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
        log.info("Finding products by category id.");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        User user = userRepository.findUserByEmail(login).orElseThrow(() -> new NotFoundException("User not found!"));
        List<SubProduct> usersFavourites = user.getFavorites();

        List<SubCategory> subCategories = jdbcTemplate.query("SELECT sub.id,sub.name,sub.category_id FROM sub_categories sub WHERE sub.category_id=?",
                new Object[]{categoryId},
                new BeanPropertyRowMapper<>(SubCategory.class));
        List<CatalogProductsResponse> catalogProductsResponse = new ArrayList<>();
        String sql = """
                 SELECT prod.id, sub.id as subId, sub.colour,sub.price,sub.quantity,prod.name,prod.rating,prod.created_at,COALESCE(dis.percent, 0) AS discount,
                        (SELECT i.images FROM sub_product_images i WHERE i.sub_product_id = sub.id LIMIT 1) AS image,
                        CASE WHEN dis.percent IS NOT NULL THEN ROUND(sub.price - (sub.price * dis.percent / 100))
                             ELSE sub.price END AS new_price,
                        (SELECT count(r) as reviews_count FROM products p join reviews r on p.id = r.product_id WHERE product_id=prod.id) as reviews_count,
                        (SELECT char.characteristics FROM sub_product_characteristics char LEFT JOIN sub_products sp ON char.sub_product_id = sp.id WHERE char.characteristics_key='память' AND sub_product_id = sub.id LIMIT 1) as memory
                 FROM sub_products sub
                        JOIN products prod ON sub.product_id = prod.id
                        %7$s JOIN discounts dis ON sub.discount_id = dis.id
                        %1$s
                        %11$s
                 WHERE sub_category_id=? %2$s  %3$s  %4$s  %5$s  %6$s  %8$s %9$s %10$s
                 LIMIT ?
                """;
        sql = filteringAndSorting(sql, brand, priceFrom, priceTo, colour,
                memory, RAM, watch_material, gender, sortBy);

        List<ColourResponse> colourResponses = new ArrayList<>();

        if (subCategoryId.isEmpty()) {
            for (SubCategory subCategory : subCategories) {
                List<CatalogProductsResponse> subProducts = jdbcTemplate.query(sql,
                        new Object[]{subCategory.getId(), pageSize},
                        (resultSet, i) -> rowMapper(resultSet));
                catalogProductsResponse.addAll(subProducts);
            }

            return getCatalogResponse(usersFavourites, catalogProductsResponse, colourResponses);
        }

        SubCategory subCategory = subCategories
                .stream()
                .filter(subCat -> Objects.equals(subCat.getId(), subCategoryId.get()))
                .findAny()
                .orElseThrow(() -> new NotFoundException("Товары с этой категорией и идентификатором подкатегории не найдены!"));

        catalogProductsResponse.addAll(jdbcTemplate.query(sql,
                new Object[]{subCategory.getId(), pageSize},
                (resultSet, i) -> rowMapper(resultSet)));
        log.info("Продукция успешно получена!");
        return getCatalogResponse(usersFavourites, catalogProductsResponse, colourResponses);
    }

    private CatalogResponse getCatalogResponse(List<SubProduct> usersFavourites,
                                               List<CatalogProductsResponse> catalogProductsResponse,
                                               List<ColourResponse> colourResponses) {
        log.info("Получение ответа каталога!");
        for (CatalogProductsResponse productsResponse : catalogProductsResponse) {
            productsResponse.setIsLiked(usersFavourites.stream()
                    .map(SubProduct::getId)
                    .anyMatch(productsResponse.getSub_product_id()::equals));
            productsResponse.setIsCompared(getAuthenticate().getComparisons().stream()
                    .map(SubProduct::getId)
                    .anyMatch(productsResponse.getSub_product_id()::equals));
        }

        colourResponses.add(ColourResponse.builder().id(1L).colour("black").quantity(catalogProductsResponse.stream()
                .filter(item -> Objects.equals(item.getColour(), "black")).count()).build());

        colourResponses.add(ColourResponse.builder().id(2L).colour("blue").quantity(catalogProductsResponse.stream()
                .filter(item -> Objects.equals(item.getColour(), "blue")).count()).build());

        colourResponses.add(ColourResponse.builder().id(3L).colour("white").quantity(catalogProductsResponse.stream()
                .filter(item -> Objects.equals(item.getColour(), "white")).count()).build());

        colourResponses.add(ColourResponse.builder().id(4L).colour("red").quantity(catalogProductsResponse.stream()
                .filter(item -> Objects.equals(item.getColour(), "red")).count()).build());

        colourResponses.add(ColourResponse.builder().id(5L).colour("gold").quantity(catalogProductsResponse.stream()
                .filter(item -> Objects.equals(item.getColour(), "gold")).count()).build());

        colourResponses.add(ColourResponse.builder().id(6L).colour("graphite").quantity(catalogProductsResponse.stream()
                .filter(item -> Objects.equals(item.getColour(), "graphite")).count()).build());

        colourResponses.add(ColourResponse.builder().id(7L).colour("green").quantity(catalogProductsResponse.stream()
                .filter(item -> Objects.equals(item.getColour(), "green")).count()).build());

        colourResponses.add(ColourResponse.builder().id(8L).colour("gold").quantity(catalogProductsResponse.stream()
                .filter(item -> Objects.equals(item.getColour(), "gold")).count()).build());

        colourResponses.add(ColourResponse.builder().id(9L).colour("silver").quantity(catalogProductsResponse.stream()
                .filter(item -> Objects.equals(item.getColour(), "silver")).count()).build());

        colourResponses.add(ColourResponse.builder().id(10L).colour("purple").quantity(catalogProductsResponse.stream()
                .filter(item -> Objects.equals(item.getColour(), "purple")).count()).build());

        log.info("Каталог Ответ успешно получен!");
        return CatalogResponse.builder()
                .productsResponses(catalogProductsResponse)
                .colours(colourResponses)
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
        String joiningCharacteristics = "";
        String conditionForFilterByRAM = "";
        String conditionForFilterByMaterial = "";
        String conditionForFilterByGender = "";
        if (memory != null && RAM != null) {
            String memories = stringify(memory);
            String rams = stringify(RAM);
            joiningCharacteristics = "LEFT JOIN sub_product_characteristics spc ON sub.id = spc.sub_product_id";
            conditionForFilterByMemory = String.format("AND spc.characteristics IN (%s) AND spc.characteristics_key='память'", memories);
            conditionForFilterByRAM = String.format("AND spc.characteristics IN (%s) AND spc.characteristics_key='Оперативная память'", rams);
        } else if (memory != null) {
            String memories = stringify(memory);
            joiningCharacteristics = "LEFT JOIN sub_product_characteristics spc ON sub.id = spc.sub_product_id";
            conditionForFilterByMemory = String.format("AND spc.characteristics IN (%s) AND spc.characteristics_key='память'", memories);
        } else if (RAM != null) {
            String rams = stringify(RAM);
            joiningCharacteristics = "LEFT JOIN sub_product_characteristics spc ON sub.id = spc.sub_product_id";
            conditionForFilterByRAM = String.format("AND spc.characteristics IN (%s) AND spc.characteristics_key='RAM'", rams);
        }
        if (watch_material != null) {
            String watch_materials = stringify(watch_material);
            joiningCharacteristics = "LEFT JOIN sub_product_characteristics spc ON sub.id = spc.sub_product_id";
            conditionForFilterByMaterial = String.format("AND spc.characteristics IN (%s) AND spc.characteristics_key='Материал корпуса'", watch_materials);
        }
        if (gender != null) {
            joiningCharacteristics = "LEFT JOIN sub_product_characteristics spc ON sub.id = spc.sub_product_id";
            conditionForFilterByGender = String.format("AND spc.characteristics='%s' AND spc.characteristics_key='Пол'", gender);
        }

        String orderBy = "";
        String joinTypeOfDiscount = "LEFT";
        if (sortBy != null) {
            switch (sortBy) {
                case "Новинки" ->
                        orderBy = "AND prod.created_at >= NOW() - INTERVAL '7 days' ORDER BY prod.created_at DESC";
                case "Все акции" -> joinTypeOfDiscount = "";
                case "До 50%" -> orderBy = "AND dis.percent < 50";
                case "Свыше 50%" -> orderBy = "AND dis.percent >= 50";
                case "Рекомендуемые" -> orderBy = "AND prod.rating >= 4";
                case "По увеличению цены" -> orderBy = " ORDER BY sub.price";
                case "По уменьшению цены" -> orderBy = "ORDER BY sub.price DESC";
            }
        }
        sql = String.format(sql, joiningForFilterByBrand, conditionForFilterByBrand, filterByPrice,
                filterByColour, conditionForFilterByMemory, conditionForFilterByRAM, joinTypeOfDiscount,
                conditionForFilterByMaterial, conditionForFilterByGender, orderBy, joiningCharacteristics);
        log.info("Продукция успешно получена!");
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
        log.info("rowMapper is successfully got");
        return CatalogProductsResponse.builder()
                .product_id(resultSet.getLong("id"))
                .sub_product_id(resultSet.getLong("subId"))
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
    public ProductUserResponse getProductById(Long productId, String colour) {
        log.info("Getting product by id");
        String sqlColours = """
                select sp.colour as colours from sub_products sp where sp.product_id=?
                """;
        List<String> colours = jdbcTemplate.query(sqlColours, (resultSet, i) -> resultSet.getString("colours"), productId);

        if (!colour.isBlank() && !colours.contains(colour)) {
            log.error(String.format("Product with colour - %s is not found!", colour));
            throw new NotFoundException(String.format("Товар с цветом - %s не найден!", colour));
        }
        String sql = """
                select sp.id as sub_product_id,
                       p.id as product_id,
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
                       sp.price as price,
                       p.date_of_issue  as date_of_issue,
                       p.description as description,
                       p.video as video_link,
                       case when uf is null then false else true end as isInFavorites
                from products p
                         join brands b on b.id = p.brand_id
                         join sub_products sp on p.id = sp.product_id
                         left join reviews r on p.id = r.product_id
                         left join discounts d on d.id = sp.discount_id
                         left join users_favorites uf on uf.favorites_id = sp.id and uf.user_id = ?
                where p.id =  ?  and sp.colour =  ?
                """;
        ProductUserResponse productUserResponse = new ProductUserResponse();
        jdbcTemplate.query(sql, (resulSet, i) -> {
                    productUserResponse.setSubProductId(resulSet.getLong("sub_product_id"));
                    productUserResponse.setProductId(resulSet.getLong("product_id"));
                    productUserResponse.setLogo(resulSet.getString("logo"));
                    productUserResponse.setName(resulSet.getString("product_name"));
                    productUserResponse.setQuantity(resulSet.getInt("quantity"));
                    productUserResponse.setItemNumber(resulSet.getString("item_number"));
                    productUserResponse.setRating(resulSet.getDouble("rating"));
                    productUserResponse.setCountOfReviews(resulSet.getInt("count_of_reviews"));
                    productUserResponse.setColor(resulSet.getString("color"));
                    productUserResponse.setPercentOfDiscount(resulSet.getInt("percent_of_discount"));
                    productUserResponse.setPrice(resulSet.getBigDecimal("price"));
                    productUserResponse.setDateOfIssue(resulSet.getDate("date_of_issue").toLocalDate());
                    productUserResponse.setDescription(resulSet.getString("description"));
                    productUserResponse.setVideo(resulSet.getString("video_link"));
                    productUserResponse.setInFavorites(resulSet.getBoolean("isInFavorites"));
                    return productUserResponse;
                }
                , productId
                , getAuthenticate().getId()
                , productId
                , !colour.isBlank() ? colour : colours.get(0)
        );

        productUserResponse.setColours(colours);
        String sql2 = """
                 select spc.characteristics_key as characteristics_key
                 , spc.characteristics as characteristics
                 from sub_product_characteristics spc
                          join sub_products sp on sp.id = spc.sub_product_id
                          where sp.product_id = ? and sp.colour = ?
                """;
        Map<String, String> characteristics = new LinkedHashMap<>();
        jdbcTemplate.query(sql2, (resultSet, i) ->
                        characteristics.put(resultSet.getString("characteristics_key")
                                , resultSet.getString("characteristics")),
                productId,
                !colour.isBlank() ? colour : colours.get(0)
        );
        productUserResponse.setCharacteristics(characteristics);
        String sql3 = """
                select spi.images as images
                from sub_product_images spi
                         join sub_products sp on sp.id = spi.sub_product_id
                         where sp.product_id = ? and sp.colour = ?
                """;
        List<String> images = jdbcTemplate.query(sql3, (resultSet, i) ->
                        resultSet.getString("images"),
                productId,
                !colour.isBlank() ? colour : colours.get(0));
        productUserResponse.setImages(images);
        log.info("Продукты успешно получен!");
        return productUserResponse;
    }

    @Override
    public List<ReviewsResponse> getAllReviewsByProductId(Long productId, int page) {
        String sql4 = """
                SELECT r.id AS id,
                       u.image AS userAvatar,
                       CONCAT(u.first_name, ' ', u.last_name) AS full_name,
                       r.created_at_time AS created_at,
                       r.grade AS grade,
                       r.commentary AS commentary,
                       r.answer AS answer,
                       ARRAY(SELECT ri.images FROM review_images ri WHERE ri.review_id = r.id) AS images
                FROM reviews r
                         JOIN users u ON u.id = r.user_id
                WHERE r.product_id = ?
                ORDER BY r.id DESC
                LIMIT ?
                """;
        return jdbcTemplate.query(sql4, (resultSet, i) -> {
            Array imagesArray = resultSet.getArray("images");
            String[] images = (String[]) imagesArray.getArray();

            return ReviewsResponse.builder()
                    .reviewsId(resultSet.getLong("id"))
                    .userAvatar(resultSet.getString("userAvatar"))
                    .fullName(resultSet.getString("full_name"))
                    .createdAt(resultSet.getString("created_at"))
                    .grade(resultSet.getInt("grade"))
                    .commentary(resultSet.getString("commentary"))
                    .answer(resultSet.getString("answer"))
                    .images(Arrays.asList(images))
                    .build();
        }, productId, page);
    }

    @Override
    public SimpleResponse update(Long subProductId, ProductUpdateRequest request) {
        SubProduct oldSubProduct = subProductRepository.findById(subProductId).orElseThrow(() -> {
            log.error("Sub product with id:" + subProductId + " is not found!");
            throw new NotFoundException("Подпродукт с id: " + subProductId + " не найден!");
        });

        SubCategory subCategory = subCategoryRepository.findById(request.subCategoryId())
                .orElseThrow(() -> {
                    log.error("Подкатегория с id: " + request.subCategoryId() + " не найдена!");
                    throw new NotFoundException("Подкатегория с id: " + request.subCategoryId() + " не найдена");
                });

        Brand brand = brandRepository.findById(request.brandId())
                .orElseThrow(() -> {
                    log.error("Brand with id:" + request.brandId() + " not found!");
                    throw new NotFoundException("Бренд с идентификатором: " + request.brandId() + " не найден!");
                });
        Product product = oldSubProduct.getProduct();
        product.setSubCategory(subCategory);
        product.setBrand(brand);
        product.setGuarantee(request.guarantee());
        product.setName(request.name());
        product.setDateOfIssue(request.dateOfIssue());
        product.setCreatedAt(LocalDate.now());
        product.setVideo(request.video());
        product.setPDF(request.PDF());
        product.setDescription(request.description());

        SubProductRequest s = request.subProducts();
        oldSubProduct.getCharacteristics().clear();
        oldSubProduct.addCharacteristics(s.characteristics());
        oldSubProduct.setColour(s.colour());
        oldSubProduct.setPrice(s.price());
        oldSubProduct.setQuantity(s.quantity());
        oldSubProduct.setImages(s.images());
        log.info(String.format("Продукт с id: %s и подпродукт с id: %s изменены!",
                product.getId(), oldSubProduct.getId()));

        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message(String.format("Продукт с id: %s и подпродукт с id: %s изменены!!",
                        product.getId(), oldSubProduct.getId()))
                .build();
    }

    @Override
    public SimpleResponse delete(List<Long> subProductIds) {
        for (Long id : subProductIds) {
            if (!subProductRepository.existsById(id)) {
                log.error("Sub product with id %s is not found!".formatted(subProductIds));
                throw new NotFoundException("Продукт с id: %s не найден!".formatted(subProductIds));
            }
        }

        for (Long subProductId : subProductIds) {
            for (Order order : orderRepository.findAll()) {
                for (SubProduct product : order.getSubProducts()) {
                    if (product.getId().equals(subProductId)
                            && !order.getStatus().equals(Status.DELIVERED)
                            && !order.getStatus().equals(Status.CANCEL)
                            && !order.getStatus().equals(Status.RECEIVED)) {
                        log.error("Продукт с id  %s не может быть удален, поскольку он в настоящее время в продаже."
                                .formatted(subProductIds));
                        throw new BadRequestException("Продукт с id  %s не может быть удален, поскольку он в настоящее время в продаже."
                                .formatted(subProductIds));
                    }
                }
            }
            Product product = subProductRepository.findById(subProductId)
                    .orElseThrow(
                            () -> new NotFoundException("Продукт с id: %s не найден!".formatted(subProductId)))
                    .getProduct();

            subProductRepository.deleteFromOrders(subProductId);
            subProductRepository.deleteFromBaskets(subProductId);
            subProductRepository.deleteFromComparisons(subProductId);
            subProductRepository.deleteFromFavorites(subProductId);
            subProductRepository.deleteFromLastViews(subProductId);
            subProductRepository.deleteSubProduct(subProductId);
            if (product.getSubProducts().isEmpty()){
                if (product.getReviews() != null) {
                    for (Review review : product.getReviews()) {
                        reviewRepository.deleteReviewImgByReviewId(review.getId());
                    }
                }
                reviewRepository.deleteByProductId(product.getId());
                productRepository.deleteProductById(product.getId());
            }
        }

        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Подпродукты с id: %s удалены.".formatted(subProductIds))
                .build();
    }

    private User getAuthenticate() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        log.info("Token has been taken!");
        return userRepository.findUserInfoByEmail(login).orElseThrow(() -> {
            log.error("User not found!");
            return new NotFoundException("User not found!");
        }).getUser();
    }
}