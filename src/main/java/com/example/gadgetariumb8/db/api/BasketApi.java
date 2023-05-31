package com.example.gadgetariumb8.db.api;

import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.example.gadgetariumb8.db.dto.response.SubProductBasketResponse;
import com.example.gadgetariumb8.db.service.BasketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/baskets")
@RequiredArgsConstructor
@Tag(name = "Basket API")
@CrossOrigin(origins = "*", maxAge = 3600)
public class BasketApi {
    private final BasketService basketService;

    @GetMapping
    @Operation(summary = "Get all basket", description = "this method shows the cart")
    @PreAuthorize("hasAuthority('USER')")
    public List<SubProductBasketResponse> basket() {
        return basketService.getAllBasket();
    }

    @PostMapping("/move_to_favorites")
    @Operation(summary = "Move to favorites", description = "This method moves to favorites!")
    @PreAuthorize("hasAuthority('USER')")
    public SimpleResponse moveToFavorites(@RequestBody ArrayList<Long> longList) {
        return basketService.moveToFavorite(longList);
    }

    @DeleteMapping("/delete_all")
    @Operation(summary = "Deleted all basket", description = "This method to delete all basket!")
    @PreAuthorize("hasAuthority('USER')")
    public SimpleResponse deletedToBasket(@RequestBody ArrayList<Long> longs) {
        return basketService.deleteBasket(longs);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete basket by ID", description = "This method to deleted basket by ID!")
    @PreAuthorize("hasAuthority('USER')")
    public SimpleResponse deletedToBasketById(@PathVariable Long id) {
        return basketService.deleteBasketById(id);
    }

    @PostMapping("/move_to_favorites_by_id")
    @Operation(summary = "Move to favorites by ID", description = "This method moves to favorites by ID!")
    @PreAuthorize("hasAuthority('USER')")
    public SimpleResponse moveToFavoritesById(@RequestParam Long id) {
        return basketService.moveToFavoriteById(id);
    }
}
