package com.example.gadgetariumb8.db.api;

import com.example.gadgetariumb8.db.dto.response.CompareProductResponse;
import com.example.gadgetariumb8.db.dto.response.PaginationResponse;
import com.example.gadgetariumb8.db.dto.response.ProductsResponse;
import com.example.gadgetariumb8.db.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/products")
@RequiredArgsConstructor
@Tag(name = "Product API")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProductUserApi {
    private final ProductService productService;

    @GetMapping("/discount")
    @Operation(summary = "Get discount products", description = "This method gets all discount products")
    @PermitAll
    public PaginationResponse<ProductsResponse> getAllDiscountProducts(@RequestParam(defaultValue = "1") int page,
                                                                       @RequestParam(defaultValue = "5") int pageSize) {
        return productService.getAllDiscountProducts(page, pageSize);
    }

    @GetMapping("/new")
    @Operation(summary = "Get new products", description = "This method gets new products")
    @PermitAll
    public PaginationResponse<ProductsResponse> getNewProducts(@RequestParam(defaultValue = "1") int page,
                                                               @RequestParam(defaultValue = "5") int pageSize) {
        return productService.getNewProducts(page, pageSize);
    }

    @GetMapping("/recommended")
    @Operation(summary = "Get recommended products", description = "This method gets recommended products")
    @PermitAll
    public PaginationResponse<ProductsResponse> getRecommendedProducts(@RequestParam(defaultValue = "1") int page,
                                                                       @RequestParam(defaultValue = "5") int pageSize) {
        return productService.getRecommendedProducts(page, pageSize);
    }

    @GetMapping("/compare-product")
    @Operation(summary = "To compare the product.", description = "This method to compare product.")
    @PreAuthorize("hasAuthority('USER')")
    public List<CompareProductResponse> compareProduct() {
        return productService.compare();
    }
}
