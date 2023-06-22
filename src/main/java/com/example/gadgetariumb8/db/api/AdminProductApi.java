package com.example.gadgetariumb8.db.api;

import com.example.gadgetariumb8.db.dto.request.BrandRequest;
import com.example.gadgetariumb8.db.dto.request.ProductRequest;
import com.example.gadgetariumb8.db.dto.request.ProductUpdateRequest;
import com.example.gadgetariumb8.db.dto.response.*;
import com.example.gadgetariumb8.db.model.Category;
import com.example.gadgetariumb8.db.service.BrandService;
import com.example.gadgetariumb8.db.service.CategoryService;
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
    private final BrandService brandService;
    private final CategoryService categoryService;

    @Operation(summary = "To save the product.", description = "This method to save the product.")
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public SimpleResponse saveProduct(@RequestBody @Valid ProductRequest productRequest) {
        return productService.saveProduct(productRequest);
    }

    @Operation(summary = "To get colors.", description = "This method to get colors with this code for save products")
    @GetMapping("/colors")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public List<ColorResponse> getColors(){
        return ColorResponse.getColors();
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

    @GetMapping("/get_all/categories")
    @Operation(summary = "Find all categories", description = "This endpoints the find all categories. ;)")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/get_all/{id}/brands_and_sub_categories")
    @Operation(summary = "Find all brands", description = "This endpoints the find all brands. ;)")
    @PreAuthorize("hasAuthority('ADMIN')")
    public BrandAndSubCategoryResponse getAllBrandsAndSubCategoriesByCategoryId(@PathVariable("id") Long categoryId) {
        return brandService.getAllBrands(categoryId);
    }

    @PostMapping("/add_brand")
    @Operation(summary = "Add brand!", description = "This endpoint adds new brand! ")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public SimpleResponse addBrand(@RequestBody @Valid BrandRequest request){
        return brandService.saveBrand(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update products", description = "This method to update products")
    @PreAuthorize("hasAuthority('ADMIN')")
    public SimpleResponse updateProduct(@PathVariable("id")Long subProductId,
                                        @RequestBody @Valid ProductUpdateRequest request){
        return productService.update(subProductId, request);
    }

    @DeleteMapping
    @Operation(summary = "Delete products", description = "This method to delete products")
    @PreAuthorize("hasAuthority('ADMIN')")
    public SimpleResponse deleteProduct(@RequestParam List<Long> subProductIds){
        return productService.delete(subProductIds);
    }
}
