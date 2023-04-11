package com.example.gadgetariumb8.db.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.beans.Mergeable;


import java.util.ArrayList;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static jakarta.persistence.CascadeType.*;


@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq")
    @Column(name = "id", nullable = false)
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

    private List<Product> favorites = new ArrayList<>();

    @ManyToMany(cascade = ALL)
    @JoinTable(name = "users_last_views",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "last_views_id"))
    private List<Product> lastViews = new ArrayList<>();

    @ManyToMany(cascade = ALL)
    @JoinTable(name = "users_comparisons",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "comparisons_id"))
    private List<SubProduct> comparisons = new ArrayList<>();
    @ElementCollection
    @Cascade(CascadeType.ALL)
    private Map<SubProduct,Integer> basket=new LinkedHashMap<>();
    @OneToMany(cascade = {REFRESH,DETACH,MERGE,PERSIST}, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private List<Order> order = new ArrayList<>();


    @OneToOne(cascade = ALL, orphanRemoval = true)
    @JoinColumn(name = "user_info_id")
    private UserInfo userInfo;

}