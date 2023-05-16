package com.example.gadgetariumb8.db.api;

import com.example.gadgetariumb8.db.dto.request.ProductRequest;
import com.example.gadgetariumb8.db.dto.response.PaginationResponse;
import com.example.gadgetariumb8.db.dto.response.ProductAdminResponse;
import com.example.gadgetariumb8.db.dto.response.ProductDetailsResponse;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.example.gadgetariumb8.db.service.ProductService;
import com.example.gadgetariumb8.db.service.SubProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
@Tag(name = "Product Admin API")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AdminProductApi {
    private final ProductService productService;
    private final SubProductService subProductService;

    @Operation(summary = "To save the product.", description = "This method to save the product.")
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public SimpleResponse saveProduct(@RequestBody @Valid ProductRequest productRequest) {
        return productService.saveProduct(productRequest);
    }

    @Operation(summary = "Get all products", description = "This method to find all products in admin page")
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public PaginationResponse<ProductAdminResponse> getAll(@RequestParam(required = false) String keyWord,
                                                           @RequestParam(defaultValue = "все товары") String status,
                                                           @RequestParam(required = false) LocalDate from,
                                                           @RequestParam(required = false) LocalDate before,
                                                           @RequestParam(required = false) String sortBy,
                                                           @RequestParam(required = false, defaultValue = "1") int page,
                                                           @RequestParam(required = false, defaultValue = "7") int pageSize) {
        return productService.getAll(keyWord, status, from, before, sortBy, page, pageSize);
    }

    @GetMapping("/{id}/product_details")
    @Operation(summary = "Get product details", description = "This method to get product details by product id")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public List<ProductDetailsResponse> productDetails(@PathVariable("id") Long productId) {
        return subProductService.getProductDetails(productId);
    }
}
