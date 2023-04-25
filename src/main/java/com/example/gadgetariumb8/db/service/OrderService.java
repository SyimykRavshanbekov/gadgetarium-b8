package com.example.gadgetariumb8.db.service;

import com.example.gadgetariumb8.db.dto.response.OrderResponse;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {
    List<OrderResponse> getAllOrders(String keyWord, String status, LocalDate from, LocalDate before, int page, int pageSize);

}
