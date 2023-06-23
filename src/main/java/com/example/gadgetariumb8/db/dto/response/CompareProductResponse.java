package com.example.gadgetariumb8.db.dto.response;

import lombok.*;

import java.math.BigDecimal;
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CompareProductResponse {
    private Long productId;
    private Long subProductId;
    private String img;
    private String name;
    private String description;
    private BigDecimal price;
    private String brandName;
    private String screen;
    private String color;
    private String operatingSystem;
    private String memory;
    private String RAM;
    private String simCard;
}
