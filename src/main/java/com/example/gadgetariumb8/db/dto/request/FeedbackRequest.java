package com.example.gadgetariumb8.db.dto.request;

import java.util.List;

/**
 * @author kurstan
 * @created at 04.07.2023 9:34
 */
public record FeedbackRequest(
        Long productId,
        int grade,
        String comment,
        List<String> images
) {
}
