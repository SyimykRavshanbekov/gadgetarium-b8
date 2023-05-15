package com.example.gadgetariumb8.db.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class ImageUrlValidator implements ConstraintValidator<ImageUrlValid, String> {
    private final List<String> allowedExtensions = Arrays.asList("png", "jpg", "jpeg");

    @Override
    public void initialize(ImageUrlValid constraintAnnotation) {
    }

    @Override
    public boolean isValid(String imageUrl, ConstraintValidatorContext constraintValidatorContext) {
        if (imageUrl == null) {
            return true;
        }
        String extension = imageUrl.substring(imageUrl.lastIndexOf(".") + 1).toLowerCase();

        return allowedExtensions.contains(extension);
    }
}
