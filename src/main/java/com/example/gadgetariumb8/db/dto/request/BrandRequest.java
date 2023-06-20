package com.example.gadgetariumb8.db.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record BrandRequest(
        @NotBlank(message = "Имя должно быть указано!!!")
        String name,
        @NotBlank(message = "Логотип должен быть указан!!!")
        String logo
) {
}
