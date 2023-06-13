package com.example.gadgetariumb8.db.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

/**
 * @author kurstan
 * @created at 27.04.2023 16:37
 */
public record AnswerRequest(
        Long reviewId,
        @NotBlank(message = "Ответ не должен быть пустым")
        @Length(min = 2, max = 300, message = "Ответ должен быть от 2 до 300 символов")
        String answer
) {
}
