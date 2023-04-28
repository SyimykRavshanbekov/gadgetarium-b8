package com.example.gadgetariumb8.db.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class ProductsResponse {
    private Long subProductId;
    private String image;
    private int quantity;
    private String productInfo;
    private double rating;
    private BigDecimal price;
    private int discount;
}
