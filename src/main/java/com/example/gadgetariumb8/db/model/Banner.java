package com.example.gadgetariumb8.db.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * name : kutman
 **/
@Getter
@Setter
@Entity
@Table(name = "banners")
@NoArgsConstructor
@AllArgsConstructor
public class Banner {

    @Id
    @SequenceGenerator(name = "banner_gen", sequenceName = "banner_seq",
            allocationSize = 1, initialValue = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "banner_gen")
    private Long id;
    @Column(length = 1000000)
    private String banner;

    public Banner(String banner) {
        this.banner = banner;
    }
}
