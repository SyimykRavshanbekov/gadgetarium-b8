package com.example.gadgetariumb8.db.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * @author kurstan
 * @created at 13.04.2023 10:07
 */
public record AuthenticateRequest(
        @NotBlank(message = "Email should not be empty!")
        String email,
        @NotBlank(message = "Password should not be empty!")
        String password
) {
}
