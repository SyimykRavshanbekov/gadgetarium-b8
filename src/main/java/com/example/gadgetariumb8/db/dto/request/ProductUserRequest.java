package com.example.gadgetariumb8.db.dto.request;

public record ProductUserRequest(
        Long productId,
        String color,
        int quantity
) {
}
