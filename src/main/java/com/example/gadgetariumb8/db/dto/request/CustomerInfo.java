package com.example.gadgetariumb8.db.dto.request;

import com.example.gadgetariumb8.db.validation.NameValid;
import com.example.gadgetariumb8.db.validation.PhoneNumberValid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CustomerInfo(
        @NotBlank(message = "first name should not be empty")
        @NameValid(message = "first name should be only letters and length must be between 2 and 33 characters!")
        String firstName,
        @NotBlank(message = "last name should not be empty")
        @NameValid(message = "last name should be only letters and length must be between 2 and 33 characters!")
        String lastName,
        @NotBlank(message = "email should not be empty")
        @Email(message = "Write valid email!")
        String email,
        @NotBlank(message = "phone number should not be empty")
        @PhoneNumberValid(message = "Phone number should start with +996, consist of 13 characters and must be valid!")
        String phoneNumber,
        String address
) {
}
