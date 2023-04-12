package com.example.gadgetariumb8.db.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "discounts")
@NoArgsConstructor
@AllArgsConstructor

public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "discount_seq")
    @SequenceGenerator(name = "discount_seq")
    @Column(name = "id", nullable = false)
    private Long id;
    private int percent;
    private LocalDate dateOfStart;
    private LocalDate dateOfFinish;


}