package com.example.gadgetariumb8.db.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AuthenticateRequest(
        @NotBlank(message = "Email should not be empty!")
        String email,
        @NotBlank(message = "Password should not be empty!")
        String password
) {
}
