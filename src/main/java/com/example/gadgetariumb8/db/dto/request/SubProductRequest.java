package com.example.gadgetariumb8.db.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record SubProductRequest(
        @NotBlank(message = "Цвет должен быть указан!!!")
        String colour,
        @NotEmpty(message = "Характеристики должны быть указаны!!!")
        Map<String, String> characteristics,
        @NotNull(message = "Цена должна быть указана!!!")
        @Min(value = 0, message = "Цена должна быть положительным числом!!")
        BigDecimal price,
        @Min(value = 1, message = "Минимальное количество не должно быть меньше 1")
        @NotNull(message = "Необходимо указать количество!!!")
        int quantity,
        @Size(min = 2, max = 6, message = "Размер должен быть от 2 до 6.")
        @NotEmpty(message = "Изображения должны быть указаны!!!")
        List<String> images
) {
}
