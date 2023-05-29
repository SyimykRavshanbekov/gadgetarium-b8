package com.example.gadgetariumb8.db.service;

import com.example.gadgetariumb8.db.dto.response.*;

import java.util.List;

public interface SubProductService {
    PaginationResponse<SubProductResponse> lastViews(int page, int pageSize);
    List<ProductDetailsResponse> getProductDetails(Long productId);
}
