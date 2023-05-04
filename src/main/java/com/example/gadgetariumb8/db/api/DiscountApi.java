package com.example.gadgetariumb8.db.api;

import com.example.gadgetariumb8.db.dto.request.DiscountRequest;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.example.gadgetariumb8.db.service.DiscountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/discounts")
@RequiredArgsConstructor
@Tag(name = "Discount API")
@CrossOrigin(origins = "*", maxAge = 3600)
public class DiscountApi {
    private final DiscountService discountService;

    @Operation(summary = "Add discount", description = "This method to add discount to products")
    @PostMapping
    public SimpleResponse addDiscount(@RequestBody @Valid DiscountRequest discountRequest) {
        return discountService.addDiscount(discountRequest);
    }
}
