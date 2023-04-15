package com.example.gadgetariumb8.db.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * @author kurstan
 * @created at 23.03.2023 12:37
 */
public class PasswordValidator implements ConstraintValidator<PasswordValid, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s.matches("^(?=.*[A-Z])(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$");
    }
}
