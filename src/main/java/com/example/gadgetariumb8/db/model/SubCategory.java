package com.example.gadgetariumb8.db.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.CascadeType.*;

@Getter
@Setter
@Entity
@Table(name = "sub_categories")
@NoArgsConstructor
@AllArgsConstructor
public class SubCategory {
    @Id
    @SequenceGenerator(name = "sub_category_gen", sequenceName = "sub_category_seq",
            allocationSize = 1, initialValue = 40)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sub_category_gen")
    private Long id;
    private String name;

    @ManyToOne(cascade = {REFRESH, PERSIST, MERGE, DETACH})
    @JoinColumn(name = "category_id")
    private Category category;
}