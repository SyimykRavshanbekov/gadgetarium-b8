package com.example.gadgetariumb8.db.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record ProductUpdateRequest(
        @NotNull(message = "Sub category must be specified!!!")
        Long subCategoryId,
        @NotNull(message = "Brand must be specified!!!")
        Long brandId,
        @Min(value = 0, message = "Guarantee should be positive number!!")
        @NotNull(message = "Guarantee must be specified!!!")
        int guarantee,
        @NotBlank(message = "Name must be specified!!!")
        String name,
        @Past(message = "Date must be in past tense!!")
        @NotNull(message = "Date of Issue must be specified!!!")
        LocalDate dateOfIssue,
        @NotBlank(message = "Video must be specified!!!")
        String video,
        @NotBlank(message = "PDF must be specified!!!")
        String PDF,
        @NotBlank(message = "Description must be specified!!!")
        String description,
        @Valid
        @NotNull(message = "Sub product doesn't be null")
        SubProductRequest subProducts
) {
}
