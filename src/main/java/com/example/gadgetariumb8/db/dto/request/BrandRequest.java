package com.example.gadgetariumb8.db.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record BrandRequest(
        @NotBlank(message = "Name must be specified!!!")
        String name,
        @NotBlank(message = "Logo must be specified!!!")
        String logo
) {
}
