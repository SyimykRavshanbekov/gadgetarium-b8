package com.example.gadgetariumb8.db.dto.response;

import lombok.Builder;

@Builder
public record ProfileResponse(
        String firstName,
        String lastName,
        String phoneNumber,
        String email,
        String address
) {
}
