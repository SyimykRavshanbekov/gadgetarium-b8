package com.example.gadgetariumb8.db.model;

import com.example.gadgetariumb8.db.model.enums.PaymentType;
import com.example.gadgetariumb8.db.model.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.*;

@Getter
@Setter
@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @SequenceGenerator(name = "order_gen", sequenceName = "order_seq",
            allocationSize = 1, initialValue = 20)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_gen")
    private Long id;
    private LocalDate date;
    private int quantity;
    private BigDecimal totalPrice;
    @Enumerated(EnumType.STRING)
    private Status status;
    private boolean deliveryType;
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;
    private String orderNumber;

    @ManyToMany(cascade = {REFRESH, PERSIST, MERGE, DETACH}, fetch = FetchType.EAGER)
    @JoinTable(name = "orders_sub_products",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "sub_products_id"))
    private List<SubProduct> subProducts;

    public void addAllSubProducts(List<SubProduct> subProducts){
        if (this.subProducts == null){
            this.subProducts = new ArrayList<>();
        }
        this.subProducts.addAll(subProducts);
    }

    @OneToOne(cascade = ALL)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(cascade = {REFRESH, PERSIST, MERGE, DETACH})
    @JoinColumn(name = "user_id")
    private User user;
}