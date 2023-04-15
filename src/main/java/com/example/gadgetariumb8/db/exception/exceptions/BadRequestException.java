package com.example.gadgetariumb8.db.exception.exceptions;

/**
 * @author kurstan
 * @created at 14.04.2023 17:27
 */
public class BadRequestException extends RuntimeException {
    public BadRequestException() {
        super();
    }

    public BadRequestException(String message) {
        super(message);
    }
}
