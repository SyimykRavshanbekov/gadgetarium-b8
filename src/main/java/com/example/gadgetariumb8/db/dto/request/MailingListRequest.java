package com.example.gadgetariumb8.db.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record MailingListRequest(
        @NotBlank(message = "Изображение не должно быть пустым!")
        String image,
        @NotBlank(message = "Имя не должно быть пустым!")
        String name,
        @NotBlank(message = "Описание не должно быть пустым!")
        String description,
        @NotNull(message = "Дата не должна быть пустой!")
        LocalDate dateOfStart,
        @Future(message = "Дата должна быть в будущем времени!")
        @NotNull(message = "Дата не должна быть пустой!")
        LocalDate dateOfFinish
) {
}
