package com.example.gadgetariumb8.db.api;

import com.example.gadgetariumb8.db.dto.response.*;
import com.example.gadgetariumb8.db.service.PdfService;
import com.example.gadgetariumb8.db.service.ProductService;
import com.example.gadgetariumb8.db.service.SubProductService;
import com.example.gadgetariumb8.db.service.impl.UserServiceImpl;
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
    private final SubProductService subProductService;
    private final PdfService pdfService;
    private final UserServiceImpl userService;


    @GetMapping("/pdf/generate/{id}")
    @PermitAll
    public String generatePDF(@PathVariable("id") Long subProductId) {
        return pdfService.exportPdf(subProductId);
    }

    @GetMapping("/get-by-id")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @Operation(summary = "To get by product id the product.", description = "This method to get by product id  the product.")
    public ProductUserResponse getByProductId(@RequestParam Long productId,
                                              @RequestParam(defaultValue = "", required = false) String colour) {
        return productService.getProductById(productId, colour);
    }

    @GetMapping("/get_all_reviews_by_product_id/{id}")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @Operation(summary = "To get all reviews by product id the product.", description = "This method to get all reviews by product id  the product.")
    public List<ReviewsResponse> getAllReviewsByProductId(@PathVariable("id") Long productId,
                                                          @RequestParam(defaultValue = "3", required = false) int page) {
        return productService.getAllReviewsByProductId(productId, page);
    }

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

    @GetMapping("/last_views")
    @Operation(summary = "Last viewed products ", description = "This method shows the last 7 items viewed")
    @PreAuthorize("hasAuthority('USER')")
    public PaginationResponse<SubProductResponse> findAllSubProductLastViews(@RequestParam(defaultValue = "1") int page,
                                                                             @RequestParam(defaultValue = "5") int pageSize) {
        return subProductService.lastViews(page, pageSize);
    }

    @GetMapping("/chosen_one")
    @Operation(summary = "Chosen One User", description = "This method chosen one user profile")
    @PermitAll
    public List<UserChosenOneResponse> getAllChosenOne() {
        return userService.getAll();
    }


}