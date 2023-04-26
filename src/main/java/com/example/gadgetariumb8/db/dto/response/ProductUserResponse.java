package com.example.gadgetariumb8.db.dto.response;

import com.example.gadgetariumb8.db.model.Brand;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Builder
public record ProductUserResponse(
        String  logo,
        List<String> images,
        String name,
        int quantity,
        String itemNumber,
        double rating,
        int countOfReviews,
        String color,
        int percentOfDiscount,
        BigDecimal price,
        BigDecimal oldPrice,
        LocalDate dateOfIssue,
        Map<String,String> characteristics,
        String Pdf,
        String description,
        String video









) {
}
