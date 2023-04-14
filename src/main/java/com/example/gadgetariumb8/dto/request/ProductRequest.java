package com.example.gadgetariumb8.dto.request;

import java.time.LocalDate;
import java.util.List;

public record ProductRequest(
        Long subCategoryId,
        Long brandId,
        int guarantee,
        String name,
        LocalDate dateOfIssue,
        String video,
        String PDF,
        String description,
        List<SubProductRequest>subProducts

) {
}
