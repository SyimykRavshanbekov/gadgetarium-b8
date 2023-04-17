package com.example.gadgetariumb8.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record ProductRequest(
        @NotNull(message = "Sub category must be specified!!!")
        Long subCategoryId,
        @NotNull(message = "Brand must be specified!!!")
        Long brandId,
        @NotNull(message = "Guarantee must be specified!!!")
        int guarantee,
        @Size(max = 15)
        @NotBlank(message = "Name must be specified!!!")
        String name,
        @NotNull(message = "Date of Issue must be specified!!!")
        LocalDate dateOfIssue,
        @NotBlank(message = "Video must be specified!!!")
        String video,
        @NotBlank(message = "PDF must be specified!!!")
        String PDF,
        @Size(max = 200)
        @NotBlank(message = "Description must be specified!!!")
        String description,
        @NotNull(message = "Description must be specified!!!")
        List<SubProductRequest> subProducts

) {
}
