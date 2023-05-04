package com.example.gadgetariumb8.db.dto.request;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

public record DiscountRequest(
        List<Long> productsId,
        @NotNull(message = "Percent doesn't be null")
        @Positive(message = "Percent must be only positive number")
        byte percentOfDiscount,
        @NotNull(message = "Date of start doesn't be null")
        LocalDate dateOfStart,
        @NotNull(message = "Date of finish doesn't be null")
        @Future(message = "Date of finish must be in future tense")
        LocalDate dateOfFinish
) {
}
