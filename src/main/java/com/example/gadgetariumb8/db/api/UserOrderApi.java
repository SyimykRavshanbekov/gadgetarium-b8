package com.example.gadgetariumb8.db.api;

import com.example.gadgetariumb8.db.dto.request.UserOrderRequest;
import com.example.gadgetariumb8.db.dto.response.UserOrderResponse;
import com.example.gadgetariumb8.db.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/orders")
@Tag(name = "User's order API")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserOrderApi {
    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Order products", description = "This method validates the request and to order products")
    @PreAuthorize("hasAuthority('USER')")
    public UserOrderResponse ordering(@RequestBody UserOrderRequest userOrderRequest) {
        return orderService.ordering(userOrderRequest);
    }
}
