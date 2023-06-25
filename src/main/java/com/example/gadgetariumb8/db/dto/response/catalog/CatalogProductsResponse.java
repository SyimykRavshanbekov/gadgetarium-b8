package com.example.gadgetariumb8.db.dto.response.catalog;

import lombok.*;

import java.math.BigDecimal;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CatalogProductsResponse{
        private Long product_id;
        private Long sub_product_id;
        private int discount;
        private int quantity;
        private String fullname;
        private String image;
        private double rating;
        private String colour;
        private int reviews_count;
        private BigDecimal price;
        private BigDecimal new_price;
        private Boolean isNew;
        private Boolean isLiked;
        private Boolean isCompared;
}
