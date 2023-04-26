package com.example.gadgetariumb8.db.service.impl;

import com.example.gadgetariumb8.db.dto.response.ProductsResponse;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.example.gadgetariumb8.db.exception.exceptions.NotFoundException;
import com.example.gadgetariumb8.db.model.Product;
import com.example.gadgetariumb8.db.model.User;
import com.example.gadgetariumb8.db.repository.ProductRepository;
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
    private final ProductRepository productRepository;
    @Override
    public SimpleResponse addProductToFavourites(Long userId, Long productId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with id: " + userId + " is no exist!"));
        Product product = productRepository.findById(productId).orElseThrow(() -> new NotFoundException("Product with id: " + productId + " is no exist!"));
        user.addFavourites(product);
        return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("Product is successfully added to favourites!").build();
    }

    @Override
    public List<ProductsResponse> getAllFavouriteProducts(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with id: " + userId + " is no exist!"));
        List<ProductsResponse> productsResponses = new ArrayList<>();
        for (Product favourite : user.getFavorites()) {
            ProductsResponse product = new ProductsResponse(
                    favourite.getSubProducts().get(0).getImages().get(0),
                    favourite.getSubProducts().get(0).getQuantity(),
                    favourite.getSubCategory().getCategory().getName() + " " + favourite.getBrand() + " " + favourite.getName() + " " + favourite.getSubProducts().get(0).getCharacteristics().get("memory") + " " + favourite.getSubProducts().get(0).getColour(),
                    favourite.getRating(),
                    favourite.getSubProducts().get(0).getPrice(),
                    favourite.getSubProducts().get(0).getPrice().intValue() - ((favourite.getSubProducts().get(0).getPrice().intValue() * favourite.getDiscount().getPercent()) / 100)
            );
            productsResponses.add(product);
        }
        return productsResponses;
    }

    @Override
    public SimpleResponse deleteById(Long userId, Long productId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with id: " + userId + " is no exist!"));
        user.getFavorites().removeIf(favorite -> favorite.getId().equals(productId));
        return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("Product is successfully deleted from favourites!").build();
    }

    @Override
    public SimpleResponse deleteAll(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with id: " + userId + " is no exist!"));
        user.setFavorites(null);
        return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("Favourites are successfully cleaned!").build();
    }
}
