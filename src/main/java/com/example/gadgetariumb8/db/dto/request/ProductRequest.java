package com.example.gadgetariumb8.db.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

public record ProductRequest(
        @NotNull(message = "Подкатегория должна быть указана!!!")
        Long subCategoryId,
        @NotNull(message = "Марка должна быть указана!!!")
        Long brandId,
        @Min(value = 0, message = "Гарантия должна быть положительной!")
        @NotNull(message = "Гарантия должна быть указана!!!")
        int guarantee,
        @NotBlank(message = "Имя должно быть указано!!!")
        String name,
        @Past(message = "Дата должна быть в прошедшем времени!!")
        @NotNull(message = "Дата выпуска должна быть указана!!!")
        LocalDate dateOfIssue,
        @NotBlank(message = "Видео должно быть указано!!!")
        String video,
        @NotBlank(message = "PDF должно быть указано!!!")
        String PDF,
        @NotBlank(message = "Описание должно быть указано!!!")
        String description,
        @Valid
        @NotEmpty(message = "Под продукты не должны быть пустыми!!!")
        List<SubProductRequest> subProducts
) {
}
