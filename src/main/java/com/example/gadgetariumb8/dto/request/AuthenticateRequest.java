package com.example.gadgetariumb8.dto.request;

/**
 * @author kurstan
 * @created at 13.04.2023 10:07
 */
public record AuthenticateRequest(
        String email,
        String password
) {
}
