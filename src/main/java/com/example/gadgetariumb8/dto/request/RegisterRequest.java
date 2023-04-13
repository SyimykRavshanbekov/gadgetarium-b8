package com.example.gadgetariumb8.dto.request;

/**
 * @author kurstan
 * @created at 13.04.2023 10:06
 */
public record RegisterRequest(
        String firstName,
        String lastName,
        String phoneNumber,
        String email,
        String password
) {
}
