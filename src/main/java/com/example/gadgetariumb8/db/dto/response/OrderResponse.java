package com.example.gadgetariumb8.db.dto.response;

import com.example.gadgetariumb8.db.model.enums.Status;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record OrderResponse(
        Long id,
        String fullName,
        String orderNumber,
        LocalDate date,
        int quantity,
        BigDecimal totalPrice,
        boolean deliveryType,
        String status

) {
}
