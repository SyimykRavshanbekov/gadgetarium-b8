package com.example.gadgetariumb8.db.service;

import com.example.gadgetariumb8.db.dto.response.ProductsResponse;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;

import java.util.List;

public interface FavouriteService {
    SimpleResponse addOrDeleteFavourites(Boolean addOrDelete, Long subProductId);

    List<ProductsResponse> getAllFavouriteProducts();

    SimpleResponse moveToFavorite(List<Long> longs);

    SimpleResponse deleteAll();
}
