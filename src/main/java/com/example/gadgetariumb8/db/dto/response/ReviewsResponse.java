package com.example.gadgetariumb8.db.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record ReviewsResponse(
        Long reviewsId,
        String userAvatar,
        String fullName,
        String createdAt,
        int grade,
        String commentary,
        List<String> images,
        String answer
) {
}
