package com.example.gadgetariumb8.db.api;

import com.example.gadgetariumb8.db.dto.response.ProductsResponse;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.example.gadgetariumb8.db.service.FavouriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favourites")
@RequiredArgsConstructor
@Tag(name = "Favourite Api")
@CrossOrigin(origins = "*", maxAge = 3600)
public class FavouritesApi {
    private final FavouriteService favouriteService;

    @PostMapping("/{subProductId}")
    @Operation(summary = "Add to favourite!", description = "This method adds product to favourites!")
    @PreAuthorize("hasAnyAuthority('USER')")
    public SimpleResponse addProductToFavourites(@PathVariable Long subProductId) {
        return favouriteService.addProductToFavourites(subProductId);
    }

    @GetMapping
    @Operation(summary = "Get all favourites!", description = "This method gets all favourite products!")
    @PreAuthorize("hasAnyAuthority('USER')")
    public List<ProductsResponse> getAllFavouriteProducts() {
        return favouriteService.getAllFavouriteProducts();
    }

    @DeleteMapping("/{subProductId}")
    @Operation(summary = "Delete by id", description = "This method removes product by id from favourites!")
    @PreAuthorize("hasAnyAuthority('USER')")
    public SimpleResponse deleteFavouriteProductById(@PathVariable Long subProductId) {
        return favouriteService.deleteById(subProductId);
    }

    @DeleteMapping
    @Operation(summary = "Delete all", description = "This method removes all favourite products")
    @PreAuthorize("hasAnyAuthority('USER')")
    public SimpleResponse deleteAllFavouriteProducts() {
        return favouriteService.deleteAll();
    }
}
