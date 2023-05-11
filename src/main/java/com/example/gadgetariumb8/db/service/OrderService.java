package com.example.gadgetariumb8.db.service;

import com.example.gadgetariumb8.db.dto.response.OrderResponse;
import com.example.gadgetariumb8.db.dto.response.PaginationResponse;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;

import java.time.LocalDate;

public interface OrderService {
    PaginationResponse<OrderResponse> getAllOrders(String keyWord, String status, LocalDate from, LocalDate before, int page, int pageSize);

    SimpleResponse changeStatusOfOrder(Long orderId, String status);
}
