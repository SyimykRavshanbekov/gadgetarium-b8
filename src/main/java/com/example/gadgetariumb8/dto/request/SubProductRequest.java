package com.example.gadgetariumb8.dto.request;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record SubProductRequest(
        String colour,
        Map<String, String> characteristics,
        BigDecimal price,
        int quantity,
        List<String> images
) {
}
