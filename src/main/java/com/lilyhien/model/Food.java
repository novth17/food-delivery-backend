package com.lilyhien.model;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String description;
    private Long price;

    //many food have the same category
    //one category have many food,  pizza has multiple types of pizza, pho also
    @ManyToOne
    private Category foodCategory;

    @Column(length = 1000)
    @ElementCollection //create a separate table for food images
    private List<String> images;

    private boolean isAvailable;

    @ManyToOne
    private Restaurant restaurant;

    private boolean isVegetarian;
    private boolean isSeasonal;

    //inside 1 food have many ingredient
    //multiple ingredient can belong to many food
    @ManyToMany
    private List<IngredientsItem> ingredients = new ArrayList<>();

    private LocalDate creationDate;
}
