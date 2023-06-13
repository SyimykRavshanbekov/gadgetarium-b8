package com.example.gadgetariumb8.db.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MailingListSubscriberRequest(
        @NotBlank(message = "Почта не должна быть пустой")
        @Email(message = "Пожалуйста, напишите действительный адрес электронной почты")
        String userEmail
) {
}
