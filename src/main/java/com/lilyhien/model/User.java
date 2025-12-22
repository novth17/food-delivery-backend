package com.lilyhien.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lilyhien.dto.RestaurantDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String fullName;

    private String email;
    private String password;
    private USER_ROLE role;

    //When you are turning this object into JSON, skip this specific field. Do not send it to the user
    //for security and prevent infinite json loop
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer") //Just use the existing customer_id column inside the Order table to connect us.
    private List<Order> orders =  new ArrayList<>();

    @ElementCollection
    private List<RestaurantDto> favorites = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true) // if delete this user, all of his address also be deleted
    private List<Address> addresses = new ArrayList<>();
}
