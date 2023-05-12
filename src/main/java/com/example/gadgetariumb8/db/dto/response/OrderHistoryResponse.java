package com.example.gadgetariumb8.db.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record OrderHistoryResponse(
        Long order_id,
        LocalDate date,
        String orderNumber,
        String status,
        BigDecimal totalPrice
){
}
