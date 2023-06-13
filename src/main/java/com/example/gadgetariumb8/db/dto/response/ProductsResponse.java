package com.example.gadgetariumb8.db.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class ProductsResponse {
    private Long subProductId;
    private String image;
    private int quantity;
    private String productInfo;
    private double rating;
    private int countOfReviews;
    private BigDecimal price;
    private int discount;
    private LocalDate createdAt;
    private boolean isInFavorites;
    private boolean isInComparisons;
}
