package com.example.gadgetariumb8.db.dto.response;

import lombok.*;

import java.util.List;
import java.util.Map;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CatalogResponse {
    private List<CatalogProductsResponse> productsResponses;
    private Map<String,Long> colourQuantity;
    private int productQuantity;
}
