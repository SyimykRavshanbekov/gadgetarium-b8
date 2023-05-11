package com.example.gadgetariumb8.db.dto.request;

import com.example.gadgetariumb8.db.model.enums.PaymentType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record UserOrderRequest(
        Map<Long, Integer> productsIdAndQuantity,
        @NotNull
        boolean deliveryType,
        @Valid
        CustomerInfo customerInfo,
        @NotNull
        PaymentType paymentType
) {
}
