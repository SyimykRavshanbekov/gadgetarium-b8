package com.example.gadgetariumb8.db.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
@Getter
@Setter
@Entity
@Table(name = "malling_list")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MallingList {
    @Id
    @SequenceGenerator(name = "malling_list_gen", sequenceName = "malling_list_seq",
            allocationSize = 1, initialValue = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "malling_list_gen")
    private Long id;
    @Column(length = 1000000)
    private String image;
    private String name;
    private String description;
    private LocalDate dateOfStart;
    private LocalDate dateOfFinish;
}