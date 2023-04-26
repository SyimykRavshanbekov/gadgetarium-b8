package com.example.gadgetariumb8.db.api;

import com.example.gadgetariumb8.db.dto.request.ProductRequest;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.example.gadgetariumb8.db.dto.response.SubProductResponse;
import com.example.gadgetariumb8.db.service.ProductService;
import com.example.gadgetariumb8.db.service.SubProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
@Tag(name = "Product Admin API")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProductAdminApi {
    private final ProductService productService;
    private final SubProductService subProductService;

    @PostMapping("/add-product")
    @Operation(summary = "To save the product.", description = "This method to save the product.")
    @PreAuthorize("hasAuthority('ADMIN')")
    public SimpleResponse saveProduct(@RequestBody @Valid ProductRequest productRequest) {
        return productService.saveProduct(productRequest);
    }

    @GetMapping
    @Operation(summary = "Last viewed products ", description = "This method shows the last 7 items viewed")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<SubProductResponse>findAllSubProductLastViews(@RequestParam Long userId){
        return subProductService.lastViews(userId);
    }
}
