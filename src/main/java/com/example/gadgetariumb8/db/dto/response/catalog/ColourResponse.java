package com.example.gadgetariumb8.db.dto.response.catalog;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ColourResponse {
    private Long id;
    private String colour;
    private Long quantity;
}
