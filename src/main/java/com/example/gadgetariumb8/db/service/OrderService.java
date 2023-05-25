package com.example.gadgetariumb8.db.service;

import com.example.gadgetariumb8.db.dto.request.UserOrderRequest;
import com.example.gadgetariumb8.db.dto.response.OrderResponse;
import com.example.gadgetariumb8.db.dto.response.PaginationResponse;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.example.gadgetariumb8.db.dto.response.UserOrderResponse;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;

import java.time.LocalDate;

public interface OrderService {
    PaginationResponse<OrderResponse> getAllOrders(String keyWord, String status, LocalDate from, LocalDate before, int page, int pageSize);

    UserOrderResponse ordering(UserOrderRequest userOrderRequest);

    SimpleResponse changeStatusOfOrder(Long orderId, String status);

    SimpleResponse delete(Long orderId);
}
