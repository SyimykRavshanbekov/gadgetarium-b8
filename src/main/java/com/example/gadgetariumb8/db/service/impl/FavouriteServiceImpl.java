package com.example.gadgetariumb8.db.service.impl;

import com.example.gadgetariumb8.db.dto.response.ProductsResponse;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.example.gadgetariumb8.db.exception.exceptions.NotFoundException;
import com.example.gadgetariumb8.db.model.SubProduct;
import com.example.gadgetariumb8.db.model.User;
import com.example.gadgetariumb8.db.repository.FavouriteRepository;
import com.example.gadgetariumb8.db.repository.SubProductRepository;
import com.example.gadgetariumb8.db.repository.UserRepository;
import com.example.gadgetariumb8.db.service.FavouriteService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FavouriteServiceImpl implements FavouriteService {
    private final SubProductRepository subProductRepository;
    private final UserRepository userRepository;
    private final FavouriteRepository favouriteRepository;

    @Override
    public SimpleResponse addOrDeleteFavourites(Boolean addOrDelete, Long subProductId) {
        return favouriteRepository.addOrDeleteFavourites(addOrDelete, subProductId);
    }

    @Override
    public List<ProductsResponse> getAllFavouriteProducts() {
        return favouriteRepository.getAllFavouriteProducts();
    }

    @Override
    @Transactional
    public SimpleResponse moveToFavorite(List<Long> longs) {
        User user = getAuthenticate();
        boolean isNotExist = true;
        for (Long id : longs) {
            SubProduct subProduct = subProductRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Подпродукт с id: "+ id + " не найден!!!"));
            for (SubProduct favoriteProducts : user.getFavorites()) {
                if (subProduct.getId().equals(favoriteProducts.getId())) {
                    isNotExist = false;
                    break;
                }
            }
            if (isNotExist) {
                user.addFavourites(subProduct);
            }
            isNotExist = true;
        }
        return SimpleResponse.builder()
                .message("Товары успешно перемещены в Избранное!!").httpStatus(HttpStatus.OK).build();
    }

    @Override
    public SimpleResponse deleteAll() {
        return favouriteRepository.deleteAll();
    }

    private User getAuthenticate() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        log.info("Токен взят!");
        return userRepository.findUserInfoByEmail(login).orElseThrow(() -> {
            log.error("Пользователь не найден с токеном пожалуйста войдите или зарегистрируйтесь!");
            return new NotFoundException("пользователь не найден с токеном пожалуйста войдите или зарегистрируйтесь");
        }).getUser();
    }

}
