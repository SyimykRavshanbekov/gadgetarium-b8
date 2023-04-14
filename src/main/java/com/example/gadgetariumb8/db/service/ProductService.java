package com.example.gadgetariumb8.db.service;

import com.example.gadgetariumb8.dto.request.ProductRequest;
import com.example.gadgetariumb8.dto.response.SimpleResponse;

public interface ProductService {
    SimpleResponse saveProduct(ProductRequest productRequest);
}
