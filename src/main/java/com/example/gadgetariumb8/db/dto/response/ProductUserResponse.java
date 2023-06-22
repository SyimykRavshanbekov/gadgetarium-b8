package com.example.gadgetariumb8.db.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductUserResponse {
    private Long productId;
    private Long subProductId;
    private String logo;
    private List<String> images;
    private List<String> colours;
    private String name;
    private int quantity;
    private String itemNumber;
    private double rating;
    private int countOfReviews;
    private String color;
    private int percentOfDiscount;
    private BigDecimal price;
    private LocalDate dateOfIssue;
    private Map<String, String> characteristics;
    private String description;
    private String video;
    private boolean isInFavorites;

}
