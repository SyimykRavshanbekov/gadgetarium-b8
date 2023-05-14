package com.example.gadgetariumb8.db.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Builder
public record UserOrderHistoryResponse(
        String orderNumber,
        List<ProductsResponse> orderedProducts,
        String status,
        String client,
        String firstName,
        String region,
        String address,
        String tel_number,
        String email,
        LocalDate date,
        String payment_type,
        String lastName,
        String city,
        BigDecimal discountPrice,
        BigDecimal totalPrice
){
}
