package com.example.gadgetariumb8.db.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SimpleResponse {
    private String description;
    private HttpStatus status;
}
