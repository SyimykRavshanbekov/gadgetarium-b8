package com.example.gadgetariumb8.db.service.impl;

import com.example.gadgetariumb8.db.dto.response.*;
import com.example.gadgetariumb8.db.exception.exceptions.NotFoundException;
import com.example.gadgetariumb8.db.model.Review;
import com.example.gadgetariumb8.db.model.SubProduct;
import com.example.gadgetariumb8.db.model.User;
import com.example.gadgetariumb8.db.repository.SubProductRepository;
import com.example.gadgetariumb8.db.repository.UserRepository;
import com.example.gadgetariumb8.db.service.SubProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubProductServiceImpl implements SubProductService {
    private final SubProductRepository subProductRepository;
    private final UserRepository userRepository;
    private final JdbcTemplate jdbcTemplate;

    private User getAuthenticate() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        log.info("Токен взят!");
        return userRepository.findUserInfoByEmail(login).orElseThrow(() -> {
            log.error("Пользователь не найден с токеном пожалуйста войдите или зарегистрируйтесь!");
            return new NotFoundException("пользователь не найден с токеном пожалуйста войдите или зарегистрируйтесь");
        }).getUser();
    }

    @Override
    public PaginationResponse<SubProductResponse> lastViews(int page, int pageSize) {
        log.info("Getting last views!");
        List<SubProductResponse> subProductResponseList = new LinkedList<>();
        List<SubProduct> subProductList = subProductRepository.getAllLastReviews(getAuthenticate().getId());
        for (SubProduct subProduct : subProductList) {
            subProductResponseList.add(new SubProductResponse(
                    subProduct.getImages().stream().findFirst().orElse("Изображения субпродуктов"),
                    subProduct.getProduct().getName(),
                    subProduct.getProduct().getBrand().getName(),
                    (subProduct.getProduct().getReviews().stream().mapToDouble(Review::getGrade).sum() / (long) subProduct.getProduct().getReviews().size()),
                    subProduct.getProduct().getReviews().size(),
                    subProduct.getPrice()
            ));
        }
        log.info("Последние просмотры успешно получены!");
        return PaginationResponse.<SubProductResponse>builder()
                .elements(subProductResponseList)
                .totalPages(pageSize)
                .currentPage(page)
                .build();
    }

    @Override
    public List<ProductDetailsResponse> getProductDetails(Long productId) {
        String sql = """
                SELECT sp.id,
                (SELECT spi.images FROM sub_product_images spi WHERE spi.sub_product_id = sp.id LIMIT 1) AS image,
                CONCAT(b.name, ' ', p.name) AS name, sp.colour, sp.quantity, sp.price,
                spc.characteristics_key, spc.characteristics
                FROM products p
                JOIN sub_products sp ON p.id = sp.product_id
                JOIN sub_product_characteristics spc ON spc.sub_product_id = sp.id
                JOIN brands b ON b.id = p.brand_id
                WHERE p.id = ?
                """;

        return jdbcTemplate.query(sql, (resultSet) -> {

            Map<Long, ProductDetailsResponse> resultMap = new HashMap<>();

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");

                ProductDetailsResponse product = resultMap.get(id);
                if (product == null) {
                    String image = resultSet.getString("image");
                    String name = resultSet.getString("name");
                    String colour = resultSet.getString("colour");
                    int quantity = resultSet.getInt("quantity");
                    BigDecimal price = resultSet.getBigDecimal("price");

                    product = new ProductDetailsResponse(id, image, name, colour, new HashMap<>(), quantity, price);
                    resultMap.put(id, product);
                }

                String key = resultSet.getString("characteristics_key");
                String value = resultSet.getString("characteristics");
                product.getCharacteristics().put(key, value);
            }

            return (List<ProductDetailsResponse>) new ArrayList<>(resultMap.values());
        }, productId);
    }
}
