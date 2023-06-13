package com.example.gadgetariumb8.db.dto.request;


import com.example.gadgetariumb8.db.validation.ImageUrlValid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ProfileImageRequest(
        @NotNull(message = "Изображение профиля не может быть пустым")
        @ImageUrlValid
        String imageUrl
) {
}