package com.example.gadgetariumb8.db.api;

import com.example.gadgetariumb8.db.dto.response.CompareProductResponse;
import com.example.gadgetariumb8.db.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/products")
@RequiredArgsConstructor
@Tag(name = "Product User API")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProductUserApi {
    private final ProductService productService;

    @GetMapping("/compare-product")
    @Operation(summary = "To compare the product.", description = "This method to compare product.")
    @PreAuthorize("hasAuthority('USER')")
    public List<CompareProductResponse> compareProduct() {
        return productService.compare();
    }
}
