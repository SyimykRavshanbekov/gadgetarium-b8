package com.example.gadgetariumb8.db.service;

import com.example.gadgetariumb8.db.dto.response.BrandAndSubCategoryResponse;

public interface BrandService {
    BrandAndSubCategoryResponse getAllBrands(Long categoryId);
}
