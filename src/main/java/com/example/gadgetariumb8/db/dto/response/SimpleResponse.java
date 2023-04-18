package com.example.gadgetariumb8.db.dto.response;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimpleResponse {
        HttpStatus httpStatus,
        String message
) {
}