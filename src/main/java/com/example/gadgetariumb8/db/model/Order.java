package com.example.gadgetariumb8.db.model;

import com.example.gadgetariumb8.db.model.enums.PaymentType;
import com.example.gadgetariumb8.db.model.enums.Status;
import jakarta.persistence.*;
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
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_seq")
    @SequenceGenerator(name = "order_seq")
    @Column(name = "id", nullable = false)
    private Long id;
    private LocalDate date;
    private int quantity;
    private BigDecimal totalPrice;
    private Status status;
    private boolean deliveryType;
    private PaymentType paymentType;
    private String orderNumber;

    @ManyToMany(cascade = {REFRESH,PERSIST,MERGE,DETACH},fetch = FetchType.EAGER)
    @JoinTable(name = "orders_sub_products",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "sub_products_id"))
    private List<SubProduct> subProducts = new ArrayList<>();

    @ManyToOne(cascade = ALL)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public Order(LocalDate date, int quantity, BigDecimal totalPrice, Status status, boolean deliveryType, PaymentType paymentType, String orderNumber) {
        this.date = date;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.status = status;
        this.deliveryType = deliveryType;
        this.paymentType = paymentType;
        this.orderNumber = orderNumber;
    }
}