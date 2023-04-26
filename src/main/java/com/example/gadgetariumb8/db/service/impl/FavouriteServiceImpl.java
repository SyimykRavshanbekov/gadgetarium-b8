package com.example.gadgetariumb8.db.service.impl;

import com.example.gadgetariumb8.db.dto.response.ProductsResponse;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.example.gadgetariumb8.db.exception.exceptions.NotFoundException;
import com.example.gadgetariumb8.db.model.Product;
import com.example.gadgetariumb8.db.model.SubProduct;
import com.example.gadgetariumb8.db.model.User;
import com.example.gadgetariumb8.db.repository.SubProductRepository;
import com.example.gadgetariumb8.db.repository.UserRepository;
import com.example.gadgetariumb8.db.service.FavouriteService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FavouriteServiceImpl implements FavouriteService {
    private final UserRepository userRepository;
    private final SubProductRepository subProductRepository;
    @Override
    public SimpleResponse addProductToFavourites(Long userId, Long subProductId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with id: " + userId + " is no exist!"));
        SubProduct subProduct= subProductRepository.findById(subProductId).orElseThrow(() -> new NotFoundException("SubProduct with id: " + subProductId + " is no exist!"));
        user.addFavourites(subProduct);
        return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("Product is successfully added to favourites!").build();
    }

    @Override
    public List<ProductsResponse> getAllFavouriteProducts(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with id: " + userId + " is no exist!"));
        List<ProductsResponse> productsResponses = new ArrayList<>();
        for (SubProduct favourite : user.getFavorites()) {
            ProductsResponse product = new ProductsResponse(
                    favourite.getImages().get(0),
                    favourite.getQuantity(),
                    favourite.getProduct().getSubCategory().getCategory().getName() + " " + favourite.getProduct().getBrand() + " " + favourite.getProduct().getName() + " " + favourite.getCharacteristics().get("memory") + " " + favourite.getColour(),
                    favourite.getProduct().getRating(),
                    favourite.getPrice(),
                    favourite.getPrice().intValue() - ((favourite.getPrice().intValue() * favourite.getProduct().getDiscount().getPercent()) / 100)
            );
            productsResponses.add(product);
        }
        return productsResponses;
    }

    @Override
    public SimpleResponse deleteById(Long userId, Long subProductId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with id: " + userId + " is no exist!"));
        user.getFavorites().removeIf(favorite -> favorite.getId().equals(subProductId));
        return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("SubProduct is successfully deleted from favourites!").build();
    }

    @Override
    public SimpleResponse deleteAll(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with id: " + userId + " is no exist!"));
        user.setFavorites(null);
        return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("Favourites are successfully cleaned!").build();
    }
}
