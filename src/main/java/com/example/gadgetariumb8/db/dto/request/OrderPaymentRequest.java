package com.example.gadgetariumb8.db.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderPaymentRequest {
    private double price;
    private String currency;
    private String description;
}
