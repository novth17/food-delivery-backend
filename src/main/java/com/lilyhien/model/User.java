package com.lilyhien.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lilyhien.dto.FavoriteRestaurantDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Full name is required")
    private String fullName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // password should not show on FE
    private String password;

    private USER_ROLE role = USER_ROLE.ROLE_CUSTOMER;

    //When you are turning this object into JSON, skip this specific field. Do not send it to the user
    //for security and prevent infinite json loop
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer") //Just use the existing customer_id column inside the Order table to connect us.
    private List<Order> orders =  new ArrayList<>();

    @ElementCollection
    private Set<FavoriteRestaurantDto> favorites = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true) // if delete this user, all of his address also be deleted
    private List<Address> addresses = new ArrayList<>();
}
