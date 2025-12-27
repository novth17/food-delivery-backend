package com.lilyhien.service;

import com.lilyhien.dto.FavoriteRestaurantDto;
import com.lilyhien.model.Restaurant;
import com.lilyhien.model.User;
import com.lilyhien.requestDto.CreateRestaurantRequest;

import java.util.List;

public interface RestaurantService {

    public Restaurant createRestaurant(CreateRestaurantRequest request, User user);

    public Restaurant updateRestaurant(Long restaurantId, CreateRestaurantRequest updatedRestaurant) throws Exception;

    public void deleteRestaurant(Long restaurantId) throws Exception;

    //only for admin role
    public List<Restaurant> getAllRestaurant();

    public List<Restaurant> searchRestaurant(String keyword);

    public Restaurant findRestaurantById(Long restaurantId) throws Exception;

    public Restaurant getRestaurantByUserId(Long userId) throws Exception;

    public FavoriteRestaurantDto addToFavorites(Long restaurantId, User user) throws Exception;

    //only for restaurant owner
    public Restaurant updateRestaurantStatus(Long id) throws Exception;
}
