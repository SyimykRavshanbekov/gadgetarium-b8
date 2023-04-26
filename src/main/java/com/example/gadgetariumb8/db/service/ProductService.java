package com.example.gadgetariumb8.db.service;


import com.example.gadgetariumb8.db.dto.request.ProductRequest;
import com.example.gadgetariumb8.db.dto.response.PaginationResponse;
import com.example.gadgetariumb8.db.dto.response.ProductAdminResponse;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;

import java.time.LocalDate;

public interface ProductService {

    SimpleResponse saveProduct(ProductRequest productRequest);

    PaginationResponse<ProductAdminResponse> getAll(String keyWord, String status, LocalDate from, LocalDate before,
                                                    String sortBy, int page, int pageSize);

}
