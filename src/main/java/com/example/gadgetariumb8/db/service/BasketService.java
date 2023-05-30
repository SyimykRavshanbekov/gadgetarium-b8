package com.example.gadgetariumb8.db.service;

import com.example.gadgetariumb8.db.dto.response.PaginationResponse;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.example.gadgetariumb8.db.dto.response.SubProductBasketResponse;

import java.util.List;

public interface BasketService {
    List<SubProductBasketResponse> getAllBasket(int page, int pageSize);

    SimpleResponse deleteBasket(List<Long> longs);

    SimpleResponse deleteBasketById(Long id);

    SimpleResponse moveToFavorite(List<Long> longs);

    SimpleResponse moveToFavoriteById(Long id);
}
