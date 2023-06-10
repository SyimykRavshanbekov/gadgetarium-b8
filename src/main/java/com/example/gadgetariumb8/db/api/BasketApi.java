package com.example.gadgetariumb8.db.api;

import com.example.gadgetariumb8.db.dto.request.BasketDeleteRequest;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.example.gadgetariumb8.db.dto.response.SubProductBasketResponse;
import com.example.gadgetariumb8.db.service.BasketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/baskets")
@RequiredArgsConstructor
@Tag(name = "Basket API")
@CrossOrigin(origins = "*", maxAge = 3600)
public class BasketApi {
    private final BasketService basketService;

    @GetMapping
    @Operation(summary = "Get all basket", description = "This method to get all products from basket")
    @PreAuthorize("hasAuthority('USER')")
    public List<SubProductBasketResponse> basket() {
        return basketService.getAllBasket();
    }

    @DeleteMapping("/delete_all")
    @Operation(summary = "Deleted all basket", description = "This method to delete all basket!")
    @PreAuthorize("hasAuthority('USER')")
    public SimpleResponse deleteAll(@RequestBody BasketDeleteRequest basketDeleteRequest) {
        return basketService.deleteBasket(basketDeleteRequest.subProductsId());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete basket by ID", description = "This method to delete basket by ID!")
    @PreAuthorize("hasAuthority('USER')")
    public SimpleResponse deleteById(@PathVariable Long id) {
        return basketService.deleteBasketById(id);
    }

    @PostMapping("/save")
    @Operation(summary = "Save basket", description = "This method to save Basket!")
    @PreAuthorize("hasAuthority('USER')")
    public SimpleResponse saveBasket(@RequestParam int quantity, @RequestParam Long subProductId) {
        return basketService.saveBasket(subProductId, quantity);
    }
}
