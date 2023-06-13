package com.example.gadgetariumb8.db.dto.request;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

public record DiscountRequest(
        List<Long> productsId,
        @NotNull(message = "Процент не будет пустым")
        @Positive(message = "Процент должен быть только положительным числом")
        byte percentOfDiscount,
        @NotNull(message = "Дата начала не может быть нулевой")
        LocalDate dateOfStart,
        @NotNull(message = "Дата окончания не может быть нулевой")
                @Future(message = "Дата окончания должна быть в будущем времени")
        LocalDate dateOfFinish
) {
}
