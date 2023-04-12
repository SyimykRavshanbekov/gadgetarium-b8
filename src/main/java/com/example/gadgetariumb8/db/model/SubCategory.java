package com.example.gadgetariumb8.db.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.CascadeType.*;


@Getter
@Setter
@Entity
@Table(name = "sub_categories")
@NoArgsConstructor
public class SubCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sub_category_seq")
    @SequenceGenerator(name = "sub_category_seq")
    @Column(name = "id", nullable = false)
    private Long id;
    private String name;

    @ManyToOne(cascade = {REFRESH, PERSIST, MERGE, DETACH})
    @JoinColumn(name = "category_id")
    private Category category;

    public SubCategory(String name) {
        this.name = name;
    }
}