package com.example.gadgetariumb8.db.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record SubProductRequest(
        @NotBlank(message = "Color must be specified!!!")
        String colour,
        @NotEmpty(message = "Characteristics must be specified!!!")
        Map<String, String> characteristics,
        @NotNull(message = "Price must be specified!!!")
        @Min(value = 0, message = "Price should be positive number!!")
        BigDecimal price,
        @Min(value = 1, message = "The minimum quantity must not be lower 1")
        @NotNull(message = "Quantity must be specified!!!")
        int quantity,
        @Size(min = 2, max = 6, message = "Size must be between 2 and 6")
        @NotEmpty(message = "Images must be specified!!!")
        List<String> images
) {
}
