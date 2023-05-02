package com.example.gadgetariumb8.db.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

/**
 * @author kurstan
 * @created at 27.04.2023 16:37
 */
public record AnswerRequest(
        Long reviewId,
        @NotBlank(message = "Answer should not be empty")
        @Length(min = 2, max = 300, message = "Answer must be between 2 and 300 characters")
        String answer
) {
}
