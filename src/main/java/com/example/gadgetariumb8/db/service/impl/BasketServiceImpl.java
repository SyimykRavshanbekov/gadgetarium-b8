package com.example.gadgetariumb8.db.service.impl;

import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.example.gadgetariumb8.db.dto.response.SubProductBasketResponse;
import com.example.gadgetariumb8.db.exception.exceptions.BadRequestException;
import com.example.gadgetariumb8.db.exception.exceptions.NotFoundException;
import com.example.gadgetariumb8.db.model.SubProduct;
import com.example.gadgetariumb8.db.model.User;
import com.example.gadgetariumb8.db.repository.SubProductRepository;
import com.example.gadgetariumb8.db.repository.UserRepository;
import com.example.gadgetariumb8.db.service.BasketService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor

public class BasketServiceImpl implements BasketService {
    private final UserRepository userRepository;
    private final JdbcTemplate jdbcTemplate;
    private final SubProductRepository subProductRepository;

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
    public List<SubProductBasketResponse> getAllBasket() {
        log.info("Получение всей корзины!");
        String sql = """
                SELECT DISTINCT (SELECT spi.images FROM sub_product_images spi WHERE spi.sub_product_id = sp.id LIMIT 1) AS img,
                       p.id AS productId, sp.id  AS subProductId, p.name AS names, sp.quantity AS quantity,
                       sp.item_number AS itemNumber, sp.price AS price,p.rating as rating,
                       (SELECT COUNT(r) FROM reviews r WHERE r.product_id = p.id) as numberOfReviews,
                       ub.basket as quantityProduct,
                       (SELECT d.percent from discounts d
                               where d.date_of_start <= CURRENT_DATE
                               AND d.date_of_finish >= CURRENT_DATE AND d.id = sp.discount_id) as percents,
                       CASE WHEN uf IS NOT NULL THEN true ELSE false END AS isInFavorites     
                FROM sub_products sp
                         JOIN products p ON p.id = sp.product_id
                         JOIN user_basket ub ON sp.id = ub.basket_key
                         JOIN users u ON ub.user_id = u.id
                         LEFT JOIN users_favorites uf ON u.id = uf.user_id AND uf.favorites_id = sp.id
                WHERE u.id = ?
                """;
        log.info("Все корзины успешно получены!");
        
        return jdbcTemplate.query(sql, (resultSet, i) ->
                new SubProductBasketResponse(
                        resultSet.getLong("productId"),
                        resultSet.getLong("subProductId"),
                        resultSet.getString("img"),
                        resultSet.getString("names"),
                        resultSet.getDouble("rating"),
                        resultSet.getInt("numberOfReviews"),
                        resultSet.getInt("quantity"),
                        resultSet.getInt("itemNumber"),
                        resultSet.getBigDecimal("price"),
                        resultSet.getInt("percents"),
                        resultSet.getInt("quantityProduct"),
                        resultSet.getBoolean("isInFavorites")
                ), getAuthenticate().getId()
        );
    }

    @Override
    public SimpleResponse deleteBasket(List<Long> subProductsId) {
        User user = userRepository.findById(getAuthenticate().getId())
                .orElseThrow(() -> new NotFoundException("Пользователь с id: "+ getAuthenticate().getId() +" не найден!!!"));
        if (subProductsId != null) {
            for (Long subProductId : subProductsId) {
                jdbcTemplate.update("DELETE FROM user_basket ub where ub.user_id = ? and ub.basket_key = ?", user.getId(), subProductId);
            }
            userRepository.save(user);
            return SimpleResponse.builder()
                    .message("Продукты успешно удалены").httpStatus(HttpStatus.OK).build();
        } else {
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message("Array of IDs is empty!")
                    .build();
        }
    }

    @Override
    public SimpleResponse deleteBasketById(Long id) {
        User user = userRepository.findById(getAuthenticate().getId())
                .orElseThrow(() -> new NotFoundException(" Пользователь с id: "+ getAuthenticate().getId() +" не найден!!!"));
        jdbcTemplate.update("DELETE FROM user_basket ub where ub.user_id = ? and ub.basket_key = ?", user.getId(), id);
        userRepository.save(user);
        return SimpleResponse.builder()
                .message("Продукт успешно удален!!").httpStatus(HttpStatus.OK).build();
    }

    @Override
    @Transactional
    public SimpleResponse saveBasket(Long subProductId, int quantity) {
        SubProduct subProduct = subProductRepository.findById(subProductId)
                .orElseThrow(() -> new NotFoundException(String.format("Продукт с идентификатором %s не найден!", subProductId)));
        User user = getAuthenticate();
        if (user.getBasket().containsKey(subProduct)) {
            throw new BadRequestException("Субпродукты с идентификатором %s уже есть в корзине".formatted(subProductId));
        }
        user.addToBasket(subProduct, quantity);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Товары успешно сохранены!!!")
                .build();
    }
}