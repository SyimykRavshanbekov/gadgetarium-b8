package com.example.gadgetariumb8.db.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumberValid, String> {

    private static final List<String> VALID_OPERATOR_PREFIXES = Arrays.asList(
            "20", "22", "50", "51", "52", "54", "55", "56", "57", "70",
            "75", "77", "800", "880", "990", "995", "996", "997", "998"
    );

    @Override
    public boolean isValid(String s, ConstraintValidatorContext context) {

        if (s.length() != 13 || !s.startsWith("+996")) {
            return false;
        }

        String operatorPrefix = s.substring(4, 7);
        return VALID_OPERATOR_PREFIXES.stream().anyMatch(operatorPrefix::contains);
    }
}
