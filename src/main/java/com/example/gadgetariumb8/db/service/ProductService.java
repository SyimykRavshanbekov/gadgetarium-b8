package com.example.gadgetariumb8.db.service;

import com.example.gadgetariumb8.db.dto.response.CatalogResponse;
import java.util.Optional;
import com.example.gadgetariumb8.db.dto.request.ProductRequest;
import com.example.gadgetariumb8.db.dto.response.*;

import java.time.LocalDate;
import java.util.List;

public interface ProductService {

    SimpleResponse saveProduct(ProductRequest productRequest);

   CatalogResponse findByCategoryIdAndFilter(Long categoryId, Optional<Long> subCategoryId,
                                                     String[] brand, String priceFrom, String priceTo,
                                                     String[] colour, String[] memory, String[] RAM, String[] watch_material,
                                                     String gender, String sortBy, int pageSize);
    List<CompareProductResponse> compare();

    PaginationResponse<ProductsResponse> getAllDiscountProducts(int page, int pageSize);

    PaginationResponse<ProductsResponse> getNewProducts(int page, int pageSize);

    PaginationResponse<ProductsResponse> getRecommendedProducts(int page, int pageSize);

    PaginationResponse<ProductAdminResponse> getAll(String keyWord, String status, LocalDate from, LocalDate before,
                                                    String sortBy, int page, int pageSize);

}
