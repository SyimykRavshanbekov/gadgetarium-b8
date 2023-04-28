package com.example.gadgetariumb8.db.service;

import com.example.gadgetariumb8.db.dto.request.ProductRequest;
import com.example.gadgetariumb8.db.dto.response.CatalogResponse;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;

import java.util.Optional;

public interface ProductService {
    SimpleResponse saveProduct(ProductRequest productRequest);

    CatalogResponse findByCategoryIdAndFilter(Long categoryId, Optional<Long> subCategoryId,
                                              String category, String priceFrom,
                                              String priceTo, String colour,
                                              String memory, String RAM, String watch_material,
                                              String gender, String sortBy, int pageSize);
}
