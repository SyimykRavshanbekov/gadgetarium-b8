package com.example.gadgetariumb8.db.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
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
    @SequenceGenerator(name = "product_gen", sequenceName = "product_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_gen")
    private Long id;
    private int guarantee;
    private String name;
    private LocalDate dateOfIssue;
    private LocalDate createdAt;
    private String video;
    private String PDF;
    private String description;
    private double rating;
    private String itemNumber;
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

    @OneToOne(cascade = ALL, orphanRemoval = true)
    @JoinColumn(name = "discount_id")
    private Discount discount;
}