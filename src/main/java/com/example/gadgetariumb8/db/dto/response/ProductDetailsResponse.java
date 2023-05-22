package com.example.gadgetariumb8.db.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
public class ProductDetailsResponse {
    private Long id;
    private String image;
    private String name;
    private String colour;
    private Map<String, String> characteristics;
    private int quantity;
    private BigDecimal price;

    public ProductDetailsResponse(Long id, String image, String name, String colour, Map<String, String> characteristics, int quantity, BigDecimal price) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.colour = colour;
        this.characteristics = characteristics;
        this.quantity = quantity;
        this.price = price;
    }
}
