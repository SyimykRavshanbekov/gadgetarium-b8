package com.example.gadgetariumb8.db.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductUserRequest {
    private Long productId;
    private String color;
    private int quantity;
    private int page;
}
