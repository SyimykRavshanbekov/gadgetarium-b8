package com.example.gadgetariumb8.db.dto.response;

import com.example.gadgetariumb8.db.model.Brand;
import lombok.Builder;

import java.util.List;

@Builder
public record BrandAndSubCategoryResponse(
        List<SubCategoryResponse> subCategories,
        List<Brand> brands
) {
}
