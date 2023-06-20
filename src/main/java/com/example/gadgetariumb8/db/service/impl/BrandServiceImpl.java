package com.example.gadgetariumb8.db.service.impl;

import com.example.gadgetariumb8.db.dto.request.BrandRequest;
import com.example.gadgetariumb8.db.dto.response.BrandAndSubCategoryResponse;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.example.gadgetariumb8.db.model.Brand;
import com.example.gadgetariumb8.db.repository.BrandRepository;
import com.example.gadgetariumb8.db.repository.SubCategoryRepository;
import com.example.gadgetariumb8.db.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
    private final SubCategoryRepository subCategoryRepository;

    @Override
    public SimpleResponse saveBrand(BrandRequest request) {
        Brand brand = new Brand();
        brand.setName(request.name());
        brand.setLogo(request.logo());
        brandRepository.save(brand);
        return new SimpleResponse(HttpStatus.OK,"Бренд успешно сохранен!");
    }

    @Override
    public BrandAndSubCategoryResponse getAllBrands(Long categoryId) {
        return BrandAndSubCategoryResponse.builder()
                .subCategories(subCategoryRepository.findAllByCategory_Id(categoryId))
                .brands(brandRepository.findAll())
                .build();
    }
}
