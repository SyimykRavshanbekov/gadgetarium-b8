package com.example.gadgetariumb8.db.service;

import com.example.gadgetariumb8.db.dto.response.OrderHistoryResponse;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.example.gadgetariumb8.db.dto.response.UserOrderHistoryResponse;

import java.util.List;

public interface UserOrderHistoryService {
    List<OrderHistoryResponse> getOrderHistory();

    UserOrderHistoryResponse getUserOrderById(Long order_id);

    SimpleResponse deleteOrderHistory();
}
