package com.example.gadgetariumb8.db.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserChosenOneResponse {
       private Long id;
       private List<String> images;
       private String productName;
       private double rating;
       private BigDecimal price;
}
