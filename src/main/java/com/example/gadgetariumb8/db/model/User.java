package com.example.gadgetariumb8.db.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;


import java.util.ArrayList;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static jakarta.persistence.CascadeType.*;

@Getter
@Setter
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @SequenceGenerator(name = "user_gen", sequenceName = "user_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_gen")
    private Long id;
    private String firstName;
    private String lastName;
    private String image;
    private String phoneNumber;
    private String address;

    @ManyToMany(cascade = ALL)
    @JoinTable(name = "users_favorites",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "favorites_id"))
    private List<SubProduct> favorites;
    public void addFavourites(SubProduct subProduct){
        if(favorites==null){
            favorites = new ArrayList<>();
        }else {
            favorites.add(subProduct);
        }
    }

    @ManyToMany(cascade = ALL)
    @JoinTable(name = "users_last_views",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "last_views_id"))
    private List<SubProduct> lastViews;

    @ManyToMany(cascade = ALL)
    @JoinTable(name = "users_comparisons",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "comparisons_id"))
    private List<SubProduct> comparisons;

    @ElementCollection
    @Cascade({CascadeType.ALL})
    private Map<SubProduct, Integer> basket;

    @OneToMany(cascade = {REFRESH, DETACH, MERGE, PERSIST})
    @JoinColumn(name = "user_id")
    private List<Order> order;

    @OneToOne(cascade = ALL, orphanRemoval = true)
    @JoinColumn(name = "user_info_id")
    private UserInfo userInfo;
    private BigDecimal bigDecimal;
}