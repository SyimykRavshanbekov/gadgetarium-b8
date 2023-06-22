package com.example.gadgetariumb8.db.service;

import com.example.gadgetariumb8.db.dto.response.catalog.CatalogProductsResponse;

import java.util.List;

public interface SearchService {
    List<CatalogProductsResponse> searchByKeyword(String keyword);
}
