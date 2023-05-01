package com.example.gadgetariumb8.db.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class ProductsResponse {
    private String image;
    private int quantity;
    private String productInfo;
    private double rating;
    private BigDecimal price;
    private int discount;
}
