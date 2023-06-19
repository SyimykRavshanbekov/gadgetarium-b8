package com.example.gadgetariumb8.db.dto.response;

import java.math.BigDecimal;

public record OrderProductResponse(
        String name,
        BigDecimal totalPrice,
        int percentOfDiscount,
        BigDecimal sumOfDiscount


) {
}
