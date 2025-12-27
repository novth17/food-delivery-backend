package com.lilyhien.repository;

import com.lilyhien.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    //JPQL - search restaurant. SD will skip parsing step
    //select java object Restaurant (r), convert to lower case
    //% find the text anywhere inside the name
    @Query("SELECT r from Restaurant r WHERE lower(r.name) LIKE lower(concat('%',:query, '%')) OR lower(r.cuisineType) LIKE lower(concat('%', :query, '%'))")
    List<Restaurant> findBySearchQuery(String query);

    Restaurant findByOwnerId(Long userId);

    Restaurant findRestaurantsById(Long id);
}
