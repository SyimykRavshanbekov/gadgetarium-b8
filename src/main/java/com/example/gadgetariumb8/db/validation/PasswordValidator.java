package com.example.gadgetariumb8.db.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<PasswordValid, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s.matches("^(?=.*[A-Z])(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$");
    }
}
