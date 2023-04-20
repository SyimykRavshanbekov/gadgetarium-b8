package com.example.gadgetariumb8.db.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * name : kutman
 **/
public record ReviewRequest(
        @NotBlank(message = "commentary should not be empty")
        @Size(min = 2, max = 500, message = "commentary must be between 2 and 500 characters")
        String commentary,
        int grade,
        @Size(min = 0, max = 5, message = "images must be between 0 and 5 characters")
        List<String>images
) {
}
