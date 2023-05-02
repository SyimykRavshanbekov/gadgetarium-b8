package com.example.gadgetariumb8.db.api;

import com.example.gadgetariumb8.db.dto.request.UserOrderRequest;
import com.example.gadgetariumb8.db.dto.response.OrderResponse;
import com.example.gadgetariumb8.db.dto.response.UserOrderResponse;
import com.example.gadgetariumb8.db.service.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/orders")
@Tag(name = "User's order API")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserOrderApi {
    private final OrderService orderService;

    public UserOrderResponse ordering(UserOrderRequest userOrderRequest) {
        return null;
    }
}
