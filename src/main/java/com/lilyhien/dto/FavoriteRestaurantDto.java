package com.lilyhien.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

//Represents a subset of data used for specific views (like a "Favorite" list or a "Search Result" card).
//https://www.geeksforgeeks.org/java/hibernate-embeddable-and-embedded-annotation/
//embeddable: goes on CLASS, this is not its own table or entity, just a reusable group of columns
@Data
@Embeddable
//tells JPA (Hibernate) that this class does not have its own table in the database, should be embedded directly into anat this class does not have its own table in the database, should be embedded directly into another tableother table
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Tell Lombok to only compare the ID
public class FavoriteRestaurantDto {

    @EqualsAndHashCode.Include
    private Long id;
    private String title;

    @Column(length = 1000)
    private String thumbnailImage;

    private String description;

}
