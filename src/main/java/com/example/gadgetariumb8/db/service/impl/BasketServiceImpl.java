package com.example.gadgetariumb8.db.service.impl;

import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.example.gadgetariumb8.db.dto.response.SubProductBasketResponse;
import com.example.gadgetariumb8.db.exception.exceptions.NotFoundException;
import com.example.gadgetariumb8.db.model.SubProduct;
import com.example.gadgetariumb8.db.model.User;
import com.example.gadgetariumb8.db.repository.SubProductRepository;
import com.example.gadgetariumb8.db.repository.UserRepository;
import com.example.gadgetariumb8.db.service.BasketService;
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
        log.info("Token has been taken!");
        return userRepository.findUserInfoByEmail(login).orElseThrow(() -> {
            log.error("User not found!");
            return new NotFoundException("User not found!");
        }).getUser();
    }

    @Override
    public List<SubProductBasketResponse> getAllBasket() {
        log.info("Getting all basket!");
        String sql = """
                SELECT (SELECT spi.images FROM sub_product_images spi WHERE spi.sub_product_id = sp.id LIMIT 1) AS img,
                       p.id                                                                                        productId,
                       sp.id                                                                                       subProductId,
                       p.name                                                                                   AS names,
                       p.description                                                                            AS description,
                       ub.basket                                                                                AS quantity,
                       sp.item_number                                                                           AS itemNumber,
                       sp.price                                                                                 AS price,
                       p.rating                                                                                 as rating,
                       (SELECT count(r2)
                        FROM products psd
                                 JOIN reviews r2 on psd.id = r2.product_id
                        where psd.id = p.id)                                                                    as numberOfReviews,
                       ub.basket                                                                                as basket,
                       (SELECT ds.percent
                               from discounts ds
                               where d.date_of_start <= CURRENT_DATE
                                 and d.date_of_finish >= CURRENT_DATE and ds.id = d.id)                                             as percents
                FROM sub_products sp
                         JOIN products p ON p.id = sp.product_id
                         JOIN user_basket ub ON sp.id = ub.basket_key
                         JOIN users u ON ub.user_id = u.id
                         JOIN discounts d on d.id = sp.discount_id
                WHERE u.id = ?
                 """;
        log.info("All baskets are successfully got!");
        return jdbcTemplate.query(sql, (resultSet, i) ->
                new SubProductBasketResponse(
                        resultSet.getLong("productId"),
                        resultSet.getLong("subProductId"),
                        resultSet.getString("img"),
                        resultSet.getString("names"),
                        resultSet.getString("description"),
                        resultSet.getDouble("rating"),
                        resultSet.getInt("numberOfReviews"),
                        resultSet.getInt("quantity"),
                        resultSet.getInt("itemNumber"),
                        resultSet.getBigDecimal("price"),
                        resultSet.getInt("percents"),
                        resultSet.getInt("basket")
                ), getAuthenticate().getId()
        );
    }

    @Override
    public SimpleResponse deleteBasket(List<Long> subProductsId) {
        User user = userRepository.findById(getAuthenticate().getId())
                .orElseThrow(() -> new NotFoundException("User with id:" + getAuthenticate().getId() + " not found!!!"));
        if (subProductsId != null) {
            for (Long subProductId : subProductsId) {
                jdbcTemplate.update("DELETE FROM user_basket ub where ub.user_id = ? and ub.basket_key = ?", user.getId(), subProductId);
            }
            userRepository.save(user);
            return SimpleResponse.builder()
                    .message("Products successfully deleted").httpStatus(HttpStatus.OK).build();
        } else {
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message("Array of IDs is empty!")
                    .build();
        }
    }

    @Override
    public SimpleResponse moveToFavorite(List<Long> longs) {
        User user = userRepository.findById(getAuthenticate().getId())
                .orElseThrow(() -> new NotFoundException("User with id:" + getAuthenticate().getId() + " not found!!!"));
        for (Long aLong : longs) {
            user.getFavorites().add(subProductRepository.findById(aLong)
                    .orElseThrow(() -> new NotFoundException("Sub product with id:" + aLong + " not found!!!")));
        }
        userRepository.save(user);
        return SimpleResponse.builder()
                .message("Products have successfully moved to Favorites").httpStatus(HttpStatus.OK).build();
    }

    @Override
    public SimpleResponse deleteBasketById(Long id) {
        User user = userRepository.findById(getAuthenticate().getId())
                .orElseThrow(() -> new NotFoundException("User with id:" + getAuthenticate().getId() + " not found!!!"));
        jdbcTemplate.update("DELETE FROM user_basket ub where ub.user_id = ? and ub.basket_key = ?", user.getId(), id);
        userRepository.save(user);
        return SimpleResponse.builder()
                .message("Product successfully deleted").httpStatus(HttpStatus.OK).build();
    }

    @Override
    public SimpleResponse moveToFavoriteById(Long id) {
        User user = userRepository.findById(getAuthenticate().getId())
                .orElseThrow(() -> new NotFoundException("User with id:" + getAuthenticate().getId() + " not found!!!"));
        user.getFavorites().add(subProductRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sub product with id:" + id + " not found!!!")));
        userRepository.save(user);
        return SimpleResponse.builder()
                .message("Products have successfully moved to Favorites").httpStatus(HttpStatus.OK).build();
    }

    @Override
    public SimpleResponse saveBasket(Long id,int quantity) {
        SubProduct subProduct = subProductRepository.findById(id)
                .orElseThrow(()->new NotFoundException(String.format("Product with id %s is not found!", id)));
        User user = userRepository.findById(getAuthenticate().getId())
                .orElseThrow(() -> new NotFoundException("User with id:" + getAuthenticate().getId() + " not found!!!"));
        user.getBasket().put(subProduct, quantity);
        userRepository.save(user);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Products have successfully saved!!!")
                .build();
    }
}
