package com.example.gadgetariumb8.db.repository.impl;

import com.example.gadgetariumb8.db.dto.response.ProductsResponse;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.example.gadgetariumb8.db.exception.exceptions.BadRequestException;
import com.example.gadgetariumb8.db.exception.exceptions.NotFoundException;
import com.example.gadgetariumb8.db.model.SubProduct;
import com.example.gadgetariumb8.db.model.User;
import com.example.gadgetariumb8.db.repository.FavouriteRepository;
import com.example.gadgetariumb8.db.repository.SubProductRepository;
import com.example.gadgetariumb8.db.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class FavouriteRepositoryImpl implements FavouriteRepository {
    private final UserRepository userRepository;
    private final SubProductRepository subProductRepository;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public SimpleResponse addOrDeleteFavourites(Boolean addOrDelete, Long subProductId) {
        User user = getAuthenticate();
        if (addOrDelete) {
            SubProduct subProduct = subProductRepository.findById(subProductId).orElseThrow(() -> new NotFoundException("SubProduct with id: " + subProductId + " is no exist!"));
            log.info("Adding favourites!");
            if (user.getFavorites().contains(subProduct)) {
                throw new BadRequestException("Sub product with id %s is already exists in favorites".formatted(subProductId));
            }
            user.addFavourites(subProduct);
            return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("Product is successfully added to favourites!").build();
        } else {
            log.info("Deleting favourites");
            user.getFavorites().removeIf(favorite -> favorite.getId().equals(subProductId));
            return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("SubProduct is successfully deleted from favourites!").build();
        }
    }

    @Override
    public List<ProductsResponse> getAllFavouriteProducts() {
        log.info("Getting all favourite products!");
        String sql = """
                SELECT sp.id AS subProductId,
                      (select i.images from sub_product_images i where i.sub_product_id = sp.id limit 1) as image,
                      sp.quantity as quantity, CONCAT(c.name, ' ', sc.name, ' ', p.name, ' ',spc.characteristics,' ', sp.colour)
                      as product_info, p.rating as rating, sp.price as price,
                      d.percent as discount,
                      p.created_at as createdAt,
                      count(r) as countOfReviews,
                      true as isInFavorites,
                      CASE WHEN uc IS NULL THEN false ELSE true END as isInComparisons
                FROM sub_products sp
                  JOIN products p ON p.id = sp.product_id
                  LEFT JOIN discounts d ON sp.discount_id = d.id
                  JOIN sub_categories sc ON p.sub_category_id = sc.id
                  JOIN categories c ON sc.category_id = c.id
                  JOIN sub_product_characteristics spc ON sp.id = spc.sub_product_id
                  LEFT JOIN reviews r ON p.id = r.product_id
                  JOIN users_favorites uf ON sp.id = uf.favorites_id
                  LEFT JOIN users_comparisons uc ON uc.comparisons_id = sp.id AND uc.user_id = ?
                WHERE spc.characteristics_key like 'память' and uf.user_id = ?
                  group by subProductId, product_info, rating, price, discount, createdAt, isInFavorites, isInComparisons;
                """;
        log.info("Favorite products are successfully got!");

        return jdbcTemplate.query(sql,
                (resultSet, i) -> new ProductsResponse(
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
    }

    @Override
    @Transactional
    public SimpleResponse deleteAll() {
        getAuthenticate().getFavorites().clear();
        log.info("Cleaned favourite products!");
        return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("Favourites are successfully cleaned!").build();
    }

    private User getAuthenticate() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        log.info("Token has been taken!");
        return userRepository.findUserByEmail(login).orElseThrow(() -> {
            log.error("User not found!");
            throw new NotFoundException("User not found!");
        });
    }
}
