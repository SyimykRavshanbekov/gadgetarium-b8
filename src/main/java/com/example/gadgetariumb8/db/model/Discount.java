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
    @SequenceGenerator(name = "discount_gen", sequenceName = "discount_seq",
            allocationSize = 1, initialValue = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "discount_gen")
    private Long id;
    private byte percent;
    private LocalDate dateOfStart;
    private LocalDate dateOfFinish;
}