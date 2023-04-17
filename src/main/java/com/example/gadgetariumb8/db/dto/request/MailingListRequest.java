package com.example.gadgetariumb8.db.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDate;

public record MailingListRequest(
        @NotBlank(message = "Image should not be empty!")
        String image,
        @NotBlank(message = "Name should not be empty!")
        String name,
        @NotBlank(message = "Description should not be empty!")
        String description,
        @NotEmpty(message = "Date should not be empty!")
        LocalDate dateOfStart,
        @Future(message = "Date must be in future tense")
        @NotEmpty(message = "Date should not be empty!")
        LocalDate dateOfFinish
) {
}
