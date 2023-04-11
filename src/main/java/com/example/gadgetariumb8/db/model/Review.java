package com.example.gadgetariumb8.db.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static jakarta.persistence.CascadeType.*;

@Getter
@Setter
@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "review_seq")
    @SequenceGenerator(name = "review_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(cascade = {REFRESH,PERSIST,MERGE,DETACH})
    @JoinColumn(name = "user_id")
    private User user;
    @ElementCollection
    private List<String> images;

    @ManyToOne(cascade = {REFRESH,PERSIST,MERGE,DETACH})
    @JoinColumn(name = "product_id")
    private Product product;
    private String commentary;
    private int grade;
    private String answer;

}