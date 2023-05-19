package com.example.gadgetariumb8.db.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record ProductAdminResponse(
        Long productId,
        Long subProductId,
        String image,
        int itemNumber,
        String name,
        LocalDate createdAt,
        int quantity,
        BigDecimal price,
        int percentOfDiscount,
        BigDecimal totalPrice
) {
}
