package com.example.gadgetariumb8.db.api;

import com.example.gadgetariumb8.db.dto.response.OrderHistoryResponse;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.example.gadgetariumb8.db.dto.response.UserOrderHistoryResponse;
import com.example.gadgetariumb8.db.service.UserOrderHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order_history")
@RequiredArgsConstructor
@Tag(name = "Order History API")
@CrossOrigin(origins = "*", maxAge = 3600)
public class OrderHistoryApi {

    private final UserOrderHistoryService orderHistoryService;

    @GetMapping()
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Getting user's order history",
            description = "This method takes all order history of authenticated user.")
    public List<OrderHistoryResponse> getOrderHistory() {
        return orderHistoryService.getOrderHistory();
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Deleting User's all order history",
            description = "This method deletes all user's order history")
    public SimpleResponse deleteAllOrderHistory() {
        return orderHistoryService.deleteOrderHistory();
    }

    @GetMapping("/{order_id}")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Get one order",
            description = "This method deletes all user's order history")
    public UserOrderHistoryResponse getOrder(@PathVariable("order_id") Long order_id) {
        return orderHistoryService.getUserOrderById(order_id);
    }
}