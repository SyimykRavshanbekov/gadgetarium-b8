package com.example.gadgetariumb8.db.dto.request;

import com.example.gadgetariumb8.db.validation.PasswordValid;
import jakarta.validation.constraints.NotBlank;

public record ProfilePasswordResetRequest(
        @NotBlank(message = "Пароль не должен быть пустым")
        String currentPassword,
        @NotBlank(message = "Новый пароль не должен быть пустым")
        @PasswordValid(message = "Длина пароля должна быть более 8 символов и содержать как минимум одну заглавную букву!")
        String newPassword
) {
}
