package com.example.gadgetariumb8.db.api;

import com.example.gadgetariumb8.db.dto.request.ProductUserRequest;
import com.example.gadgetariumb8.db.dto.response.*;
import com.example.gadgetariumb8.db.service.PdfService;
import com.example.gadgetariumb8.db.service.ProductService;
import com.example.gadgetariumb8.db.service.SubProductService;
import com.example.gadgetariumb8.db.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
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
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "To get by product id the product.", description = "This method to get by product id  the product.")
    public ProductUserResponse getByProductId(@RequestBody ProductUserRequest productUserRequest) {
        return productService.getProductById(productUserRequest);
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


    @GetMapping("/basket")
    @Operation(summary = "Get all basket", description = "this method shows the cart")
    @PermitAll
    public PaginationResponse<SubProductBasketResponse> basket(@RequestParam(defaultValue = "1") int page,
                                                               @RequestParam(defaultValue = "5") int pageSize) {
        return subProductService.getAllBasket(page, pageSize);
    }

    @PostMapping("/delete_or_move_to_favorites")
    @Operation(summary = "delete or move to favorites", description = "This method moves to favorites and deletes")
    @PermitAll
    public SimpleResponse deleteOrMoveToFavorites(@RequestBody List<Long> longList, @RequestParam String key) {
        return subProductService.deleteOrMoveToFavorites(key, longList);
    }

    @GetMapping("/compare-product")
    @Operation(summary = "To compare the product.", description = "This method to compare product.")
    @PreAuthorize("hasAuthority('USER')")
    public List<CompareProductResponse> compareProduct() {
        return productService.compare();
    }

    @GetMapping("/chosen_one")
    @Operation(summary = "Chosen One User", description = "This method chosen one user profile")
    @PermitAll
    public List<UserChosenOneResponse> getAllChosenOne() {
        return userService.getAll();
    }

    @GetMapping("/countCompare")
    @Operation(summary = "To count the Compare.", description = "This method count the Compare")
    @PermitAll
    public CompareCountResponse countCompare() {
        return productService.countCompare();
    }

    @DeleteMapping
    @Operation(summary = "To clean the Compare", description = "This method clean table Comparisons")
    @PermitAll
    public SimpleResponse cleanCompare() {
        return productService.cleanCompare();


    }
}