package com.example.gadgetariumb8.db.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class PhoneNumberValidator implements ConstraintValidator<PhoneNumberValid, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s.startsWith("+996") && s.length() == 13 && s.matches("\\+\\d+") && s.startsWith("20", 4)
                || s.startsWith("22", 4) || s.startsWith("5", 4) || s.startsWith("70", 4)
                || s.startsWith("71", 4) || s.startsWith("75", 4) || s.startsWith("77", 4)
                || s.startsWith("99", 4);
    }
}
