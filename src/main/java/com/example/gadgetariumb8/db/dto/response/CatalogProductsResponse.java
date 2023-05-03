package com.example.gadgetariumb8.db.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CatalogProductsResponse{
        Long sub_product_id;
        int discount;
        int quantity;
        String fullname;
        String image;
        double rating;
        String colour;
        int reviews_count;
        BigDecimal price;
        BigDecimal new_price;
        Boolean isNew;
        Boolean isLiked;

}
