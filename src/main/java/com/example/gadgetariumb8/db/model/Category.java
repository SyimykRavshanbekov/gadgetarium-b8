package com.example.gadgetariumb8.db.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "categories")
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @SequenceGenerator(name = "category_gen", sequenceName = "category_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_gen")
    private Long id;
    private String name;
}