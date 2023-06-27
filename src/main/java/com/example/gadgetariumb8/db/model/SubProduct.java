package com.example.gadgetariumb8.db.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static jakarta.persistence.CascadeType.*;

@Getter
@Setter
@Entity
@Table(name = "sub_products")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubProduct {
    @Id
    @SequenceGenerator(name = "sub_product_gen", sequenceName = "sub_product_seq",
            allocationSize = 1, initialValue = 40)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sub_product_gen")
    private Long id;
    private String colour;
    private BigDecimal price;
    private int quantity;
    private int itemNumber;

    @ElementCollection
    private List<String> images;

    @ElementCollection
    private Map<String, String> characteristics;

    public void addCharacteristics(Map<String, String> characteristics) {
        if (this.characteristics == null) {
            this.characteristics = new HashMap<>();
        }
        this.characteristics.putAll(characteristics);
    }

    @ManyToOne(cascade = {REFRESH, PERSIST, MERGE, DETACH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(cascade = ALL)
    @JoinColumn(name = "discount_id")
    private Discount discount;
}