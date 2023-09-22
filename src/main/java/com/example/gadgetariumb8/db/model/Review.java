package com.example.gadgetariumb8.db.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static jakarta.persistence.CascadeType.*;

@Getter
@Setter
@Entity
@Table(name = "reviews")
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    @SequenceGenerator(name = "review_gen", sequenceName = "review_seq",
    allocationSize = 1, initialValue = 40)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "review_gen")
    private Long id;
    @Column(length = 10000)
    private String commentary;
    private int grade;
    private String answer;
    private LocalDateTime createdAtTime;

    @ElementCollection
    private List<String> images;

    public void addImages(List<String> images){
        if (this.images == null){
            this.images = new ArrayList<>();
        }
        this.images.addAll(images);
    }

    @ManyToOne(cascade = {REFRESH, PERSIST, MERGE, DETACH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(cascade = {REFRESH, PERSIST, MERGE, DETACH})
    @JoinColumn(name = "user_id")
    private User user;

    public Review(String commentary, int grade, List<String> images, Product product, User user) {
        this.commentary = commentary;
        this.grade = grade;
        this.images = images;
        this.product = product;
        this.user = user;
    }
}