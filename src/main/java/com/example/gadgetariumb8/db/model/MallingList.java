package com.example.gadgetariumb8.db.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "malling_list")
@NoArgsConstructor
@AllArgsConstructor
public class MallingList {
    @Id
    @SequenceGenerator(name = "malling_list_gen", sequenceName = "malling_list_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "malling_list_gen")
    private Long id;
    private String image;
    private String name;
    private String description;
    private LocalDate dateOfStart;
    private LocalDate dateOfFinish;
}