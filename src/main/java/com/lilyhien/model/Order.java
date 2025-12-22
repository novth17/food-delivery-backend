package com.lilyhien.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


//hidden default constructor

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User customer;

    @JsonIgnore
    @ManyToOne
    private Restaurant restaurant;

    private Long totalAmount;

    private String orderStatus;

    private Date createdAt;

    @ManyToOne //one address can have many orders, but one order can belong to 1 address
    private Address deliveryAddress;

    @OneToMany //one order can have many order items
    private List<OrderItem> items = new ArrayList<>();;

    //private Payment payment;

    private int totalItem;

    private int totalPrice;

}
