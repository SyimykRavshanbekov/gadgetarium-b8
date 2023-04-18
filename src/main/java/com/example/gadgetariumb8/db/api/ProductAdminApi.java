package com.example.gadgetariumb8.db.api;

import com.example.gadgetariumb8.db.dto.request.ProductRequest;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.example.gadgetariumb8.db.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
@Tag(name = "Product Admin API")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProductAdminApi {
    private final ProductService productService;

    @PostMapping("/add-product")
    @Operation(summary = "To save the product.", description = "This method to save the product.")
    @PreAuthorize("hasAuthority('ADMIN')")
    public SimpleResponse saveProduct(@RequestBody @Valid ProductRequest productRequest) {
        return productService.saveProduct(productRequest);
    }
}
