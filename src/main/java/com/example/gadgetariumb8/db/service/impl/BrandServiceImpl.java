package com.example.gadgetariumb8.db.service.impl;

import com.example.gadgetariumb8.db.dto.response.BrandAndSubCategoryResponse;
import com.example.gadgetariumb8.db.repository.BrandRepository;
import com.example.gadgetariumb8.db.repository.SubCategoryRepository;
import com.example.gadgetariumb8.db.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
    private final SubCategoryRepository subCategoryRepository;

    @Override
    public BrandAndSubCategoryResponse getAllBrands(Long categoryId) {
        return BrandAndSubCategoryResponse.builder()
                .subCategories(subCategoryRepository.findAllByCategory_Id(categoryId))
                .brands(brandRepository.findAll())
                .build();
    }
}
