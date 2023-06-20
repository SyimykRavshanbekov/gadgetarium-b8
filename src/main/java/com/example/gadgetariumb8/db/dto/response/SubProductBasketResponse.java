package com.example.gadgetariumb8.db.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SubProductBasketResponse {
    private Long productId;
    private Long subProductId;
    private String image;
    private String name;
    private double rating;
    private int numberOfReviews;
    private int quantity;
    private int itemNumber;
    private BigDecimal price;
    private int percent;
    private int quantityProduct;
    private boolean isInFavorites;
}
