package com.example.gadgetariumb8.db.service;


import com.example.gadgetariumb8.db.dto.request.ProductRequest;
import com.example.gadgetariumb8.db.dto.response.SubProductResponse;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;

import java.util.List;

public interface ProductService {
    SimpleResponse saveProduct(ProductRequest productRequest);

}
