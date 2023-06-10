package com.example.gadgetariumb8.db.service;

import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.example.gadgetariumb8.db.dto.response.SubProductBasketResponse;

import java.util.List;

public interface BasketService {
    List<SubProductBasketResponse> getAllBasket();

    SimpleResponse deleteBasket(List<Long> subProductsId);

    SimpleResponse deleteBasketById(Long id);

    SimpleResponse saveBasket(Long id, int quantity);
}
