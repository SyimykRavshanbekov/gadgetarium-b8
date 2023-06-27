package com.example.gadgetariumb8.db.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.ArrayList;
import java.util.HashMap;
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
    @SequenceGenerator(name = "user_gen", sequenceName = "user_seq",
            allocationSize = 1, initialValue = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_gen")
    private Long id;
    private String firstName;
    private String lastName;
    @Column(length = 1000000)
    private String image;
    private String phoneNumber;
    private String address;

    @ManyToMany(cascade = ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "users_favorites",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "favorites_id"))
    private List<SubProduct> favorites;

    public void addFavourites(SubProduct subProduct) {
        if (this.favorites == null) {
            this.favorites = new ArrayList<>();
        }
        this.favorites.add(subProduct);
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

    public void addComparisons(SubProduct subProduct){
        if (this.comparisons== null){
           this.comparisons= new ArrayList<>();
        }
        this.comparisons.add(subProduct);
    }

    @ElementCollection
    @Cascade({CascadeType.ALL})
    private Map<SubProduct, Integer> basket;

    public void addToBasket(SubProduct subProduct, int quantity) {
        if (this.basket == null) {
            this.basket = new HashMap<>();
        }
        this.basket.put(subProduct, quantity);
    }

    @OneToMany(cascade = {REFRESH, DETACH, MERGE, PERSIST})
    @JoinColumn(name = "user_id")
    private List<Order> orders;

    public void addOrder(Order order) {
        if (orders == null) {
            orders = new ArrayList<>();
        }
        orders.add(order);
    }

    @OneToOne(cascade = ALL, orphanRemoval = true)
    @JoinColumn(name = "user_info_id")
    private UserInfo userInfo;

}