package com.example.gadgetariumb8.db.api;

import com.example.gadgetariumb8.db.dto.response.ProductUserResponse;
import com.example.gadgetariumb8.db.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/products")
@RequiredArgsConstructor
@Tag(name = "Product User API")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProductApi {
    private final ProductService productService;

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    @Operation(summary = "To get by product id the product.", description = "This method to get by product id  the product.")
    public ProductUserResponse getByProduct(@PathVariable Long id){
        return productService.getProductById(id);
    }

}
