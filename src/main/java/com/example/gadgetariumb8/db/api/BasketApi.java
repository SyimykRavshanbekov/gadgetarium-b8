package com.example.gadgetariumb8.db.api;

import com.example.gadgetariumb8.db.dto.response.PaginationResponse;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.example.gadgetariumb8.db.dto.response.SubProductBasketResponse;
import com.example.gadgetariumb8.db.service.BasketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/basket")
@RequiredArgsConstructor
@Tag(name = "Basket API")
@CrossOrigin(origins = "*", maxAge = 3600)
public class BasketApi {
    private final BasketService basketService;

    @GetMapping("/basket")
    @Operation(summary = "Get all basket", description = "this method shows the cart")
    @PermitAll
    public List<SubProductBasketResponse> basket() {
        return basketService.getAllBasket();
    }

    @PostMapping("/move_to_favorites")
    @Operation(summary = "Move to favorites", description = "This method moves to favorites!")
    @PermitAll
    public SimpleResponse moveToFavorites(@RequestBody ArrayList<Long> longList) {
        return basketService.moveToFavorite(longList);
    }

    @DeleteMapping("/deleted_basket")
    @Operation(summary = "Deleted to basket", description = "This method deleted to basket!")
    @PermitAll
    public SimpleResponse deletedToBasket(@RequestBody ArrayList<Long>longs) {
        return basketService.deleteBasket(longs);
    }

    @PostMapping("/deleted_basket_by_id")
    @Operation(summary = "Deleted to basket by ID", description = "This method deleted to basket by ID!")
    @PermitAll
    public SimpleResponse deletedToBasketById(@RequestParam Long id) {
        return basketService.deleteBasketById(id);
    }
    @PostMapping("/move_to_favorites_by_id")
    @Operation(summary = "Move to favorites by ID", description = "This method moves to favorites by ID!")
    @PermitAll
    public SimpleResponse moveToFavoritesById(@RequestParam Long id) {
        return basketService.moveToFavoriteById(id);
    }

}
