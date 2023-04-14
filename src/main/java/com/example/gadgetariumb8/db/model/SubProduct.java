package com.example.gadgetariumb8.db.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
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
    @SequenceGenerator(name = "sub_product_gen", sequenceName = "sub_product_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sub_product_gen")
    private Long id;
    private String colour;
    private BigDecimal price;
    private int quantity;

    @ElementCollection
    private List<String> images;

    @ElementCollection
    private Map<String, String> characteristics;

    @ManyToOne(cascade = {REFRESH, PERSIST, MERGE, DETACH})
    @JoinColumn(name = "product_id")
    private Product product;
}