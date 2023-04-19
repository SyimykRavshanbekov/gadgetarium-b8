package com.example.gadgetariumb8.db.dto.request;

import com.example.gadgetariumb8.db.validation.PasswordValid;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record ResetPasswordRequest(
        @NotBlank(message = "password should not be empty")
        @PasswordValid(message = "Password length must be more than 8 symbols," +
                " and contain least one capital letter!")
        String newPassword
){
}
