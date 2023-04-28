package com.example.gadgetariumb8.db.service;

import com.example.gadgetariumb8.db.dto.response.PaginationResponse;
import com.example.gadgetariumb8.db.dto.response.ProductsResponse;
import com.example.gadgetariumb8.db.dto.response.SubProductResponse;

import java.util.List;

public interface SubProductService {
    PaginationResponse<SubProductResponse> lastViews(int page, int pageSize);
}
