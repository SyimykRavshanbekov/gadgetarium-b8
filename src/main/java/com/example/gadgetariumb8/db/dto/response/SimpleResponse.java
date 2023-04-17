package com.example.gadgetariumb8.db.dto.response;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimpleResponse {
    private String description;
    private HttpStatus status;
}