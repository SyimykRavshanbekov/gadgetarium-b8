package com.example.gadgetariumb8.db.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * @author kurstan
 * @created at 15.04.2023 16:33
 */
public class EmailValidator implements ConstraintValidator<EmailValid, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s.endsWith("@gmail.com") || s.endsWith("@mail.ru");
    }
}
