package com.example.gadgetariumb8.db.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "brands")
@NoArgsConstructor
@AllArgsConstructor
public class Brand {
    @Id
    @SequenceGenerator(name = "brand_gen", sequenceName = "brand_seq",
            allocationSize = 1, initialValue = 20)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "brand_gen")
    private Long id;
    private String name;
    @Column(length = 1000000)
    private String logo;
}