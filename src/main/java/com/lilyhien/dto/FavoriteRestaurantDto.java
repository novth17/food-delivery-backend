package com.lilyhien.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.util.List;

//Represents a subset of data used for specific views (like a "Favorite" list or a "Search Result" card).
//https://www.geeksforgeeks.org/java/hibernate-embeddable-and-embedded-annotation/
//embeddable: goes on CLASS, this is not its own table or entity, just a reusable group of columns
@Data
@Embeddable //tells JPA (Hibernate) that this class does not have its own table in the database, should be embedded directly into anat this class does not have its own table in the database, should be embedded directly into another tableother table
public class FavoriteRestaurantDto {

    private String title;

    @Column(length = 1000)
    private List<String> images;

    private String description;
    private Long id;

}
