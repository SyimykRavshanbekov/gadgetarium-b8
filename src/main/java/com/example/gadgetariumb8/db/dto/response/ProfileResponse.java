package com.example.gadgetariumb8.db.dto.response;

public record ProfileResponse(
        String image,
        String firstName,
        String lastName,
        String phoneNumber,
        String email,
        String address
) {
}
