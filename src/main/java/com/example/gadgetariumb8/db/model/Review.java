package com.example.gadgetariumb8.db.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @SequenceGenerator(name = "review_gen", sequenceName = "review_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "review_gen")
    private Long id;
    private String commentary;
    private int grade;
    private String answer;

    @ElementCollection
    private List<String> images;

    @ManyToOne(cascade = {REFRESH, PERSIST, MERGE, DETACH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(cascade = {REFRESH, PERSIST, MERGE, DETACH})
    @JoinColumn(name = "user_id")
    private User user;

    public Review(String commentary, int grade, List<String> images) {
        this.commentary = commentary;
        this.grade = grade;
        this.images = images;
    }
}