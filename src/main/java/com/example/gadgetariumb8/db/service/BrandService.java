package com.example.gadgetariumb8.db.service;

import com.example.gadgetariumb8.db.dto.request.BrandRequest;
import com.example.gadgetariumb8.db.dto.response.BrandAndSubCategoryResponse;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;

public interface BrandService {
    SimpleResponse saveBrand(BrandRequest brand);
    BrandAndSubCategoryResponse getAllBrands(Long categoryId);
}
