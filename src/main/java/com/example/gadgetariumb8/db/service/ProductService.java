package com.example.gadgetariumb8.db.service;
import com.example.gadgetariumb8.db.dto.request.ProductRequest;
import com.example.gadgetariumb8.db.dto.response.*;

import java.time.LocalDate;
import java.util.List;

public interface ProductService {

    SimpleResponse saveProduct(ProductRequest productRequest);

    PaginationResponse<ProductsResponse> getAllDiscountProducts(int page, int pageSize);

    PaginationResponse<ProductsResponse> getNewProducts(int page, int pageSize);

    PaginationResponse<ProductsResponse> getRecommendedProducts(int page, int pageSize);

    PaginationResponse<ProductAdminResponse> getAll(String keyWord, String status, LocalDate from, LocalDate before,
                                                    String sortBy, int page, int pageSize);

}
