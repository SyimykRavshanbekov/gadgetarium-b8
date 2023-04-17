package com.example.gadgetariumb8.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record SubProductRequest(
        @NotBlank(message = "Color must be specified!!!")
        String colour,
        @NotNull(message = "Characteristics must be specified!!!")
        Map<String, String> characteristics,
        @NotNull(message = "Price must be specified!!!")
        BigDecimal price,
        @NotNull(message = "Quantity must be specified!!!")
        int quantity,
        @Size(min = 2,max = 6)
        @NotNull(message = "Images must be specified!!!")
        List<String> images
) {
}
