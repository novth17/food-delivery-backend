package com.lilyhien.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class IngredientsItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ManyToOne // owner of this rela is many side, sauce(cat) has many type of sauces
    private IngredientsCategory category;

    @JsonIgnore
    @ManyToOne
    private Restaurant restaurant;

    private Boolean isInStock = true;

}
