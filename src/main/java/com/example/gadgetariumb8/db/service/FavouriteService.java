package com.example.gadgetariumb8.db.service;

import com.example.gadgetariumb8.db.dto.response.ProductsResponse;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;

import java.util.List;

public interface FavouriteService {
    SimpleResponse addProductToFavourites(Long userId, Long subProductId);
    List<ProductsResponse> getAllFavouriteProducts(Long userId);
    SimpleResponse deleteById(Long userId, Long subProductId);
    SimpleResponse deleteAll(Long userId);


}
