package com.example.gadgetariumb8.db.api;

import com.example.gadgetariumb8.db.dto.request.ProductRequest;
import com.example.gadgetariumb8.db.dto.response.PaginationResponse;
import com.example.gadgetariumb8.db.dto.response.ProductAdminResponse;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.example.gadgetariumb8.db.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
@Tag(name = "Product Admin API")
@CrossOrigin(origins = "*", maxAge = 3600)
@PreAuthorize("hasAuthority('ADMIN')")
public class ProductAdminApi {
    private final ProductService productService;

    @Operation(summary = "To save the product.", description = "This method to save the product.")
    @PostMapping
    public SimpleResponse saveProduct(@RequestBody @Valid ProductRequest productRequest) {
        return productService.saveProduct(productRequest);
    }

    @Operation(summary = "Get all products in admin page", description = "This method ")
    @GetMapping
    public PaginationResponse<ProductAdminResponse> getAll(@RequestParam(required = false) String keyWord,
                                                           @RequestParam(defaultValue = "all") String status,
                                                           @RequestParam(required = false) LocalDate from,
                                                           @RequestParam(required = false) LocalDate before,
                                                           @RequestParam(required = false, defaultValue = "id") String sortBy,
                                                           @RequestParam(required = false, defaultValue = "1") int page,
                                                           @RequestParam(required = false, defaultValue = "7") int pageSize) {
        return productService.getAll(keyWord, status, from, before, sortBy, page, pageSize);
    }
}
