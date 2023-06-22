package com.example.gadgetariumb8.db.dto.response.catalog;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CatalogResponse {
    private List<CatalogProductsResponse> productsResponses;
    private List<ColourResponse> colours;
    private int productQuantity;
}
