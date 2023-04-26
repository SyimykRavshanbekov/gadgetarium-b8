package com.example.gadgetariumb8.db.service;


import com.example.gadgetariumb8.db.dto.request.ProductRequest;
import com.example.gadgetariumb8.db.dto.response.ProductUserResponse;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;

public interface ProductService {
    SimpleResponse saveProduct(ProductRequest productRequest);
    ProductUserResponse getProductById(Long id);
}
