package com.example.gadgetariumb8.db.dto.response;

import lombok.*;

import java.math.BigDecimal;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SubProductResponse{
    private String img;
    private String name;
    private String brand;
    private double rating;
    private int numberOfReviews;
    private BigDecimal price;
}
