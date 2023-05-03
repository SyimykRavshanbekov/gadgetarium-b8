package com.example.gadgetariumb8.db.repository.impl;

import com.example.gadgetariumb8.db.dto.response.ProductsResponse;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.example.gadgetariumb8.db.exception.exceptions.NotFoundException;
import com.example.gadgetariumb8.db.model.SubProduct;
import com.example.gadgetariumb8.db.model.User;
import com.example.gadgetariumb8.db.repository.FavouriteRepository;
import com.example.gadgetariumb8.db.repository.SubProductRepository;
import com.example.gadgetariumb8.db.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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
        if (addOrDelete.equals(true)) {
            SubProduct subProduct = subProductRepository.findById(subProductId).orElseThrow(() -> new NotFoundException("SubProduct with id: " + subProductId + " is no exist!"));
            getAuthenticate().addFavourites(subProduct);
            return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("Product is successfully added to favourites!").build();
        } else {
            getAuthenticate().getFavorites().removeIf(favorite -> favorite.getId().equals(subProductId));
            return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("SubProduct is successfully deleted from favourites!").build();
        }
    }

    @Override
    public List<ProductsResponse> getAllFavouriteProducts() {
        List<ProductsResponse> productsResponses = new ArrayList<>();
        for (SubProduct favorite : getAuthenticate().getFavorites()) {
            String sql = """
                    SELECT sp.id AS subProductId,
                        (select i.images from sub_product_images i where i.sub_product_id = sp.id limit 1) as image,
                         sp.quantity as quantity, CONCAT(c.name, ' ', p.brand_id, ' ', p.name, ' ',spc.characteristics,' ', sp.colour)
                         as product_info, p.rating as rating, sp.price as price,
                        CAST(sp.price - ((sp.price * d.percent) / 100) AS INTEGER) as discount
                    FROM sub_products sp
                        JOIN products p ON p.id = sp.product_id
                        LEFT JOIN discounts d ON p.discount_id = d.id
                        JOIN sub_categories sc ON p.sub_category_id = sc.id
                        JOIN categories c ON sc.category_id = c.id
                        JOIN sub_product_characteristics spc ON sp.id = spc.sub_product_id
                    WHERE spc.characteristics_key like 'память' and sp.id = ?;
                     """;

            productsResponses.addAll(jdbcTemplate.query(sql,
                    (resultSet, i) -> new ProductsResponse(
                            resultSet.getLong("subProductId"),
                            resultSet.getString("image"),
                            resultSet.getInt("quantity"),
                            resultSet.getString("product_info"),
                            resultSet.getDouble("rating"),
                            resultSet.getBigDecimal("price"),
                            resultSet.getInt("discount")
                    ), favorite.getId()));
        }
        return productsResponses;
    }

    @Override
    public SimpleResponse deleteAll() {
        getAuthenticate().setFavorites(null);
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
