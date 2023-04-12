package com.example.gadgetariumb8.db.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "discounts")
@NoArgsConstructor
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "discount_seq")
    @SequenceGenerator(name = "discount_seq")
    @Column(name = "id", nullable = false)
    private Long id;
    private int percent;
    private LocalDate dateOfStart;
    private LocalDate dateOfFinish;

    public Discount(int percent, LocalDate dateOfStart, LocalDate dateOfFinish) {
        this.percent = percent;
        this.dateOfStart = dateOfStart;
        this.dateOfFinish = dateOfFinish;
    }
}