package com.example.gadgetariumb8.db.api;

import com.example.gadgetariumb8.db.dto.response.catalog.CatalogResponse;
import com.example.gadgetariumb8.db.service.impl.ProductServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/catalog")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Catalog Api")
public class CatalogApi {

    private final ProductServiceImpl productService;

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping(path = {"/{category_id}", "/{category_id}/{subCategory_id}",})
    @Operation(summary = "Get all products and filter",
            description = "This method is for getting products by category and subCategory, and filter them with specific characteristics")
    public CatalogResponse getProducts(@PathVariable(value = "category_id") Long category_id,
                                       @PathVariable(value = "subCategory_id") Optional<Long> subCategory_id,
                                       @RequestParam(required = false) String[] category,
                                       @RequestParam(required = false, defaultValue = "500") String priceFrom,
                                       @RequestParam(required = false, defaultValue = "250000") String priceTo,
                                       @RequestParam(required = false) String[] colour,
                                       @RequestParam(required = false) String[] memory,
                                       @RequestParam(required = false) String[] RAM,
                                       @RequestParam(required = false) String[] watch_material,
                                       @RequestParam(required = false) String gender,
                                       @RequestParam(required = false) String sortBy,
                                       @RequestParam(required = false, defaultValue = "12") int pageSize
    ) {
        return productService.findByCategoryIdAndFilter(category_id, subCategory_id, category,
                priceFrom, priceTo, colour, memory, RAM, watch_material, gender, sortBy, pageSize
        );
    }
}