package com.lilyhien.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


//hidden default constructor

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "customer_id")
    private User customer;

    @JsonIgnore
    @ManyToOne
    private Restaurant restaurant;


    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime createdAt;

    @ManyToOne //one address can have many orders, but one order can belong to 1 address
    @JoinColumn(name = "address_id")
    private Address deliveryAddress;

    //one order can have many order items
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();;

    private Long totalPrice;

}
