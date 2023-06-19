package com.example.gadgetariumb8.db.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class OrderInfoResponse {
    Long orderId;
    int orderNumber;
    List<OrderProductResponse> products;
    int quantity;
    BigDecimal totalPrice;
    String status;
    String phoneNumber;
    String address;
    String customerName;

    public OrderInfoResponse(Long orderId, int orderNumber, int quantity, BigDecimal totalPrice, String status, String phoneNumber, String address, String customerName) {
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.status = status;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.customerName = customerName;
    }
}
