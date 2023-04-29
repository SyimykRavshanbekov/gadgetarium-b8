package com.example.gadgetariumb8.db.service.impl;

import com.example.gadgetariumb8.db.dto.response.*;
import com.example.gadgetariumb8.db.exception.exceptions.NotFoundException;
import com.example.gadgetariumb8.db.model.*;
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

import java.util.LinkedList;
import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class SubProductServiceImpl implements SubProductService {
    private final SubProductRepository subProductRepository;
    private final UserRepository userRepository;
    private final JdbcTemplate jdbcTemplate;
    private User getAuthenticate(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        log.info("Token has been taken!");
        return userRepository.findUserInfoByEmail(login).orElseThrow(()-> {
            log.error("User not found!");
            return new NotFoundException("User not found!");
        }).getUser();
    }
    @Override
    public PaginationResponse<SubProductResponse> lastViews(int page, int pageSize) {
        List<SubProductResponse>subProductResponseList = new LinkedList<>();
        List<SubProduct> subProductList = subProductRepository.getAllLastReviews(getAuthenticate().getId());
        for (SubProduct subProduct : subProductList) {
            subProductResponseList.add(new SubProductResponse(
                    subProduct.getImages().stream().findFirst().orElse("Sub product images"),
                    subProduct.getProduct().getName(),
                    subProduct.getProduct().getBrand().getName(),
                    (subProduct.getProduct().getReviews().stream().mapToDouble(Review::getGrade).sum()/ (long) subProduct.getProduct().getReviews().size()),
                    subProduct.getProduct().getReviews().size(),
                    subProduct.getPrice()
            ));
        }
        return PaginationResponse.<SubProductResponse>builder()
                .elements(subProductResponseList)
                .totalPages(pageSize)
                .currentPage(page)
                .build();
    }

    @Override
    public PaginationResponse<SubProductBasketResponse> getAllBasket(int page, int pageSize) {
        String sql = """
                SELECT (SELECT spi FROM sub_product_images spi WHERE spi.sub_product_id = sp.id LIMIT 1) AS img,
                       p.name AS names,
                       p.description AS description,
                       ub.basket AS quantity,
                       sp.item_number AS itemNumber,
                       sp.price AS price,
                       p.rating as rating,
                       (SELECT count(r2)
                        FROM products psd
                                 JOIN reviews r2 on psd.id = r2.product_id
                        where psd.id = p.id) as numberOfReviews
                FROM sub_products sp
                         JOIN products p ON p.id = sp.product_id
                         JOIN user_basket ub ON sp.id = ub.basket_key
                         JOIN users u ON ub.user_id = u.id
                WHERE u.id = 1;
                """;
        List<SubProductBasketResponse> getAll = jdbcTemplate.query(sql,(resultSet,i)->
                new SubProductBasketResponse(
                        resultSet.getString("img"),
                        resultSet.getString("names"),
                        resultSet.getString("description"),
                        resultSet.getDouble("rating"),
                        resultSet.getInt("numberOfReviews"),
                        resultSet.getInt("quantity"),
                        resultSet.getInt("itemNumber"),
                        resultSet.getBigDecimal("price")
                )
        );
        return PaginationResponse.<SubProductBasketResponse>builder()
                .elements(getAll)
                .totalPages(pageSize)
                .currentPage(page)
                .build();
    }

    @Override
    public SimpleResponse deleteOrMoveToFavorites(String key, List<Long> longs) {
        User user = userRepository.findById(getAuthenticate().getId())
                .orElseThrow(() -> new NotFoundException("User with id:" + getAuthenticate().getId() + " not found!!!"));
        switch (key) {
            case "moveToFavourite" -> {
                for (Long aLong : longs) {
                    user.getFavorites().add(subProductRepository.findById(aLong)
                            .orElseThrow(() -> new NotFoundException("Sub product with id:" + aLong + " not found!!!")));
                }
                userRepository.save(user);
                return SimpleResponse.builder()
                        .message("Products have successfully moved to Favorites").httpStatus(HttpStatus.OK).build();
            }
            case "delete" -> {
                for (Long aLong : longs) {
                    jdbcTemplate.update("DELETE FROM user_basket ub where ub.basket_key = ?", aLong);
                }
                userRepository.save(user);
                return SimpleResponse.builder()
                        .message("products successfully deleted").httpStatus(HttpStatus.OK).build();
            }
            default -> {
                return SimpleResponse.builder()
                        .message("Wrong value received").httpStatus(HttpStatus.BAD_REQUEST).build();
            }
        }
    }
}
