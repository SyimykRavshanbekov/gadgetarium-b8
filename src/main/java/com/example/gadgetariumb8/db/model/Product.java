package com.example.gadgetariumb8.db.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.*;

@Getter
@Setter
@Entity
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @SequenceGenerator(name = "product_gen", sequenceName = "product_seq",
            allocationSize = 1, initialValue = 20)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_gen")
    private Long id;
    private int guarantee;
    private String name;
    private LocalDate dateOfIssue;
    private LocalDate createdAt;
    @Column(length = 10000)
    private String video;
    @Column(length = 10000)
    private String PDF;
    @Column(length = 1000)
    private String description;
    private double rating;

    @OneToMany(cascade = ALL, fetch = FetchType.EAGER, mappedBy = "product", orphanRemoval = true)
    private List<Review> reviews;

    @ManyToOne(cascade = {DETACH, MERGE, REFRESH, PERSIST})
    @JoinColumn(name = "sub_category_id")
    private SubCategory subCategory;

    @ManyToOne(cascade = {DETACH, MERGE, REFRESH, PERSIST})
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @OneToMany(cascade = ALL, fetch = FetchType.EAGER, mappedBy = "product", orphanRemoval = true)
    private List<SubProduct> subProducts;

    public void addSubProduct(SubProduct subProduct) {
        if (subProducts == null) {
            subProducts = new ArrayList<>();
        }
        subProducts.add(subProduct);
    }
}