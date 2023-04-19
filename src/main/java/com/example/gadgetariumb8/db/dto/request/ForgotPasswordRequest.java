package com.example.gadgetariumb8.db.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record ForgotPasswordRequest(
        @NotBlank(message = "email should not be empty")
        @Email(message = "Write valid email!")
        String email
) {
}
