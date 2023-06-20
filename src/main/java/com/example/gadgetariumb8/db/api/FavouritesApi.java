package com.example.gadgetariumb8.db.api;

import com.example.gadgetariumb8.db.dto.response.ProductsResponse;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.example.gadgetariumb8.db.service.FavouriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/favourites")
@RequiredArgsConstructor
@Tag(name = "Favourite Api")
@CrossOrigin(origins = "*", maxAge = 3600)
public class FavouritesApi {
    private final FavouriteService favouriteService;

    @PostMapping("/{subProductId}")
    @Operation(summary = "Add or delete from favourites!", description = "This method add or delete products from favourites!")
    @PreAuthorize("hasAnyAuthority('USER')")
    public SimpleResponse addOrDeleteFavourites(@PathVariable Long subProductId, @RequestParam Boolean addOrDelete) {
        return favouriteService.addOrDeleteFavourites(addOrDelete, subProductId);
    }

    @GetMapping
    @Operation(summary = "Get all favourites!", description = "This method gets all favourite products!")
    @PreAuthorize("hasAnyAuthority('USER')")
    public List<ProductsResponse> getAllFavouriteProducts() {
        return favouriteService.getAllFavouriteProducts();
    }

    @PostMapping("/move_to_favorites")
    @Operation(summary = "Move to favorites", description = "This method moves to favorites!")
    @PreAuthorize("hasAuthority('USER')")
    public SimpleResponse moveToFavorites(@RequestBody ArrayList<Long> longList) {
        System.out.println(longList);
        return favouriteService.moveToFavorite(longList);
    }

    @DeleteMapping
    @Operation(summary = "Delete all", description = "This method removes all favourite products")
    @PreAuthorize("hasAnyAuthority('USER')")
    public SimpleResponse deleteAllFavouriteProducts() {
        return favouriteService.deleteAll();
    }
}
