package com.example.gadgetariumb8.db.service;

import com.example.gadgetariumb8.db.dto.request.ProductRequest;
import com.example.gadgetariumb8.db.dto.request.ProductUpdateRequest;
import com.example.gadgetariumb8.db.dto.response.*;
import com.example.gadgetariumb8.db.dto.response.catalog.CatalogResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProductService {

    SimpleResponse saveProduct(ProductRequest productRequest);

    ProductUserResponse getProductById(Long productId, String colour);

    List<ReviewsResponse> getAllReviewsByProductId(Long productId, int page);

    CatalogResponse findByCategoryIdAndFilter(Long categoryId, Optional<Long> subCategoryId,
                                              String[] brand, String priceFrom, String priceTo,
                                              String[] colour, String[] memory, String[] RAM, String[] watch_material,
                                              String gender, String sortBy, int pageSize);

    PaginationResponse<ProductsResponse> getAllDiscountProducts(int page, int pageSize);

    PaginationResponse<ProductsResponse> getNewProducts(int page, int pageSize);

    PaginationResponse<ProductsResponse> getRecommendedProducts(int page, int pageSize);

    PaginationResponse<ProductAdminResponse> getAll(String keyWord, String status, LocalDate from, LocalDate before,
                                                    String sortBy, int page, int pageSize);

    SimpleResponse update(Long subProductId, ProductUpdateRequest request);

    SimpleResponse delete(List<Long> subProductIds);
}
