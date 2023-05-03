package com.example.gadgetariumb8.db.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MailingListSubscriberRequest(
        @NotBlank(message = "This field should not be empty")
        @Email(message = "Please write valid email")
        String userEmail
) {
}
