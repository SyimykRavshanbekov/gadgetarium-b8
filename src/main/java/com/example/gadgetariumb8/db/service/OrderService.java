package com.example.gadgetariumb8.db.service;

import com.example.gadgetariumb8.db.dto.request.UserOrderRequest;
import com.example.gadgetariumb8.db.dto.response.*;

import java.time.LocalDate;

public interface OrderService {
    PaginationResponse<OrderResponse> getAllOrders(String keyWord, String status, LocalDate from, LocalDate before, int page, int pageSize);

    UserOrderResponse ordering(UserOrderRequest userOrderRequest);

    SimpleResponse changeStatusOfOrder(Long orderId, String status);

    SimpleResponse delete(Long orderId);

    OrderInfoResponse getOrderInfo(Long orderId);
}
