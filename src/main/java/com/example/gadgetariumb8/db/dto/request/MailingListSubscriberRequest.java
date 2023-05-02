package com.example.gadgetariumb8.db.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MailingListSubscriberRequest(
        @NotBlank(message = "This field should not be empty")
        @Email(message = "Field should be email, like 'example@gmail.com'")
        String userEmail
) {
}
