package com.example.gadgetariumb8.db.service.impl;

import com.example.gadgetariumb8.db.dto.response.ProductsResponse;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.example.gadgetariumb8.db.repository.impl.FavouriteRepositoryImpl;
import com.example.gadgetariumb8.db.service.FavouriteService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FavouriteServiceImpl implements FavouriteService {
    private final FavouriteRepositoryImpl favouriteRepository;

    @Override
    public SimpleResponse addOrDeleteFavourites(Boolean addOrDelete, Long subProductId) {
        return favouriteRepository.addOrDeleteFavourites(addOrDelete, subProductId);
    }

    @Override
    public List<ProductsResponse> getAllFavouriteProducts() {
        return favouriteRepository.getAllFavouriteProducts();
    }

    @Override
    public SimpleResponse deleteAll() {
        return favouriteRepository.deleteAll();
    }
}
