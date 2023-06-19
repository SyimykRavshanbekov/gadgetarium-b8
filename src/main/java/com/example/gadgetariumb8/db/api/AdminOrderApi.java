package com.example.gadgetariumb8.db.api;

import com.example.gadgetariumb8.db.dto.response.OrderInfoResponse;
import com.example.gadgetariumb8.db.dto.response.OrderResponse;
import com.example.gadgetariumb8.db.dto.response.PaginationResponse;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.example.gadgetariumb8.db.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
@Tag(name = "Order Admin API")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AdminOrderApi {
    private final OrderService orderService;

    @GetMapping
    @Operation(summary = "Get All Orders", description = "This method getAll Orders")
    @PreAuthorize("hasAuthority('ADMIN')")
    public PaginationResponse<OrderResponse> getAllOrders(@RequestParam(required = false) String keyWord,
                                                          @RequestParam(defaultValue = "PENDING") String status,
                                                          @RequestParam(required = false) LocalDate from,
                                                          @RequestParam(required = false) LocalDate before,
                                                          @RequestParam(required = false, defaultValue = "1") int page,
                                                          @RequestParam(required = false, defaultValue = "5") int pageSize) {
        return orderService.getAllOrders(keyWord, status, from, before, page, pageSize);
    }

    @PostMapping
    @Operation(summary = "Change status of orders", description = "This method to change status of orders")
    @PreAuthorize("hasAuthority('ADMIN')")
    public SimpleResponse changeStatusOfOrder(@RequestParam Long orderId, @RequestParam String status) {
        return orderService.changeStatusOfOrder(orderId, status);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete order", description = "This method to delete orders")
    @PreAuthorize("hasAuthority('ADMIN')")
    public SimpleResponse delete(@PathVariable("id") Long orderId) {
        return orderService.delete(orderId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by id", description = "This method to get order info by id!!")
    @PreAuthorize("hasAuthority('ADMIN')")
    public OrderInfoResponse getOrderInfo(@PathVariable("id") Long orderId) {
        return orderService.getOrderInfo(orderId);
    }

}
