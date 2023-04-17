package com.example.gadgetariumb8.db.dto.response;

import lombok.*;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SimpleResponse {
    private String description;
    private HttpStatus status;
}
