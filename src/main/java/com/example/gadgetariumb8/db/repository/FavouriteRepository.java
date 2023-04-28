package com.example.gadgetariumb8.db.repository;

import com.example.gadgetariumb8.db.dto.response.ProductsResponse;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;

import java.util.List;

public interface FavouriteRepository {
    SimpleResponse addOrDeleteFavourites(Boolean addOrDelete, Long subProductId);

    List<ProductsResponse> getAllFavouriteProducts();

    SimpleResponse deleteAll();
}
