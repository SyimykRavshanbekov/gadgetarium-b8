package com.example.gadgetariumb8.db.dto.request;

import java.util.List;

/**
 * name : kutman
 **/
public record ReviewRequest(
        String commentary,
        int grade,
        List<String>images
) {
}
