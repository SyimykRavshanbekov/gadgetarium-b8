package com.example.gadgetariumb8.db.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record UserChosenOneResponse(
        Long id,
        List<String> images,
        String productName,
        double rating,
        BigDecimal price
) {
}
