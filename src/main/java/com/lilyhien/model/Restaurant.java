package com.lilyhien.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


//@AllArgsConstructor Lombok writes a constructor that accepts every field. You can do it in one shot.
//Restaurant r = new Restaurant("Pizza Palace", "123 Main St", "Mario", 5);

@Data //if not this, you have to manually write getters and setters for every single field
@Entity
@NoArgsConstructor
@AllArgsConstructor //Lombok writes a constructor that accepts every field.
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private User owner;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String cuisineType;

    @OneToOne //one restaurant for one user, keep it consistent
    private Address address;

    @Embedded // add object info to field. take the fields of ContactInformation and flatten them into this entity’s table.
    private ContactInformation contactInformation;

    private String openingHours;

    private List<String> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true) //Don't create a new table. Look at the restaurant field inside the Order class to use the data.
    private List<Order> orders = new ArrayList<>();

    private int numRating;

    @Column(length = 1000)
    @ElementCollection //create a separate table for food images
    private List<String> images;

    private LocalDateTime registrationDate;
    private boolean isOpen;

    @JsonIgnore //fetch restaurant data object, dont need this food list inside object, make separate new api for fetch food in restaurant
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)// I don't control the foreign key; look at the Food entity to figure out how we are related.
    private List<Food> foods = new ArrayList<>();
}
