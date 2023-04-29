package com.example.gadgetariumb8.db.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductUserRequest {
    private Long productId;
    private String color;
    private int quantity;
}
