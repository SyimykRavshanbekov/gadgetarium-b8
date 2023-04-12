package com.example.gadgetariumb8.db.model;

import jakarta.persistence.*;
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
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq")
    @SequenceGenerator(name = "product_seq")
    @Column(name = "id", nullable = false)
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


    @OneToMany(cascade = ALL,fetch = FetchType.EAGER, mappedBy = "product", orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @ManyToOne(cascade = {DETACH,MERGE,REFRESH,PERSIST})
    @JoinColumn(name = "sub_category_id")
    private SubCategory subCategory;

    @ManyToOne(cascade = {DETACH,MERGE,REFRESH,PERSIST})
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @OneToMany(cascade = ALL, fetch = FetchType.EAGER,mappedBy = "product", orphanRemoval = true)
    private List<SubProduct> subProducts = new ArrayList<>();

    @OneToOne(cascade = ALL)
    @JoinColumn(name = "discount_id")
    private Discount discount;

    public Product(int guarantee, String name, LocalDate dateOfIssue, LocalDate createdAt, String video, String PDF, String description, double rating, String itemNumber) {
        this.guarantee = guarantee;
        this.name = name;
        this.dateOfIssue = dateOfIssue;
        this.createdAt = createdAt;
        this.video = video;
        this.PDF = PDF;
        this.description = description;
        this.rating = rating;
        this.itemNumber = itemNumber;
    }
}