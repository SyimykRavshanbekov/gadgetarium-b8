package com.example.gadgetariumb8.db.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "customers")
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @SequenceGenerator(name = "customer_gen",sequenceName = "customer_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_gen")
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
}