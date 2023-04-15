package com.example.gadgetariumb8.db.dto.request;

import com.example.gadgetariumb8.db.validation.EmailValid;
import com.example.gadgetariumb8.db.validation.PasswordValid;
import com.example.gadgetariumb8.db.validation.PhoneNumberValid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

/**
 * @author kurstan
 * @created at 13.04.2023 10:06
 */
public record RegisterRequest(
        @NotBlank(message = "first name should not be empty")
        @Size(min = 2, max = 33, message = "first name must be between 2 and 33 characters")
        String firstName,
        @NotBlank(message = "last name should not be empty")
        @Size(min = 2, max = 33, message = "last name must be between 2 and 33 characters")
        String lastName,
        @NotBlank(message = "phone number should not be empty")
        @PhoneNumberValid(message = "Phone number should start with +996, consist of 13 characters and must be valid!")
        String phoneNumber,
        @NotBlank(message = "email should not be empty")
        @EmailValid(message = "Write valid email!")
        String email,
        @NotBlank(message = "password should not be empty")
        @PasswordValid(message = "Password length must be more than 8 symbols," +
                " and contain least one capital letter!")
        String password
) {
}
