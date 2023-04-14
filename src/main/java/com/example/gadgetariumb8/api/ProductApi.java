package com.example.gadgetariumb8.api;

import com.example.gadgetariumb8.db.service.ProductService;
import com.example.gadgetariumb8.dto.request.ProductRequest;
import com.example.gadgetariumb8.dto.response.SimpleResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductApi {

    private final ProductService productService;

    public ProductApi(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping()
    @PreAuthorize("permitAll()")
    public SimpleResponse saveProduct(ProductRequest productRequest){
        return productService.saveProduct(productRequest);
    }
}
