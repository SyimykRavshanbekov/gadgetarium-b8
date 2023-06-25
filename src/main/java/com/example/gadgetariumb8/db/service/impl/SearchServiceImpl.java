package com.example.gadgetariumb8.db.service.impl;
import com.example.gadgetariumb8.db.dto.response.catalog.CatalogProductsResponse;
import com.example.gadgetariumb8.db.exception.exceptions.NotFoundException;
import com.example.gadgetariumb8.db.model.SubProduct;
import com.example.gadgetariumb8.db.model.User;
import com.example.gadgetariumb8.db.repository.UserRepository;
import com.example.gadgetariumb8.db.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchServiceImpl implements SearchService {
    private final JdbcTemplate jdbcTemplate;
    private final ProductServiceImpl productService;
    private final UserRepository userRepository;

    @Override
    public List<CatalogProductsResponse> searchByKeyword(String keyword) {
        String sql = """
                 SELECT DISTINCT prod.id, sub.id as subId,sub.colour,sub.price,sub.quantity,prod.name,prod.rating,prod.created_at,COALESCE(dis.percent, 0) AS discount,
                     (SELECT i.images FROM sub_product_images i WHERE i.sub_product_id = sub.id LIMIT 1) AS image,
                     CASE WHEN dis.percent IS NOT NULL THEN ROUND(sub.price - (sub.price * dis.percent / 100))
                             ELSE sub.price END AS new_price,
                     (SELECT count(r) as reviews_count FROM products p join reviews r on p.id = r.product_id WHERE product_id=prod.id) as reviews_count,
                     (SELECT char.characteristics FROM sub_product_characteristics char LEFT JOIN sub_products sp ON char.sub_product_id = sp.id WHERE char.characteristics_key='память' AND sub_product_id = sub.id LIMIT 1) as memory
                 FROM sub_products sub
                     JOIN products prod ON sub.product_id = prod.id
                     LEFT JOIN discounts dis ON sub.discount_id = dis.id
                     JOIN sub_categories sc ON prod.sub_category_id = sc.id
                     JOIN categories c ON sc.category_id = c.id
                     JOIN brands b ON prod.brand_id = b.id
                 WHERE %s
                """;

        String condition = "(1=1)";
        List<String> keys = new ArrayList<>();
        if (keyword != null) {
            keyword = keyword.toLowerCase();
            keys.add("%" + keyword + "%");
            keys.add("%" + keyword + "%");
            keys.add("%" + keyword + "%");
            keys.add("%" + keyword + "%");
            condition = """
                    (LOWER(prod.name) like LOWER(?)) OR (LOWER(sc.name) like LOWER(?))
                    OR (LOWER(c.name) like LOWER(?)) OR (LOWER(b.name) like LOWER(?))
                    """;
        }
        sql = String.format(sql, condition);
        List<CatalogProductsResponse> products = jdbcTemplate.query(sql, keys.toArray(),
                (resultSet, i) ->
                        productService.rowMapper(resultSet)
        );
        for (CatalogProductsResponse product : products) {
            product.setIsLiked(getAuthenticate().getFavorites().stream()
                    .map(SubProduct::getId)
                    .anyMatch(product.getSub_product_id()::equals));
            product.setIsCompared(getAuthenticate().getComparisons().stream()
                    .map(SubProduct::getId)
                    .anyMatch(product.getSub_product_id()::equals));
        }
        return products;
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
