package com.lilyhien.service;

import com.lilyhien.model.Category;
import com.lilyhien.model.Food;
import com.lilyhien.model.Restaurant;
import com.lilyhien.requestDto.CreateFoodRequest;

import java.util.List;

//If your Controller depends on an Interface, it doesn't care how the service works, only that it exists.
public interface FoodService {

    public Food createFood(CreateFoodRequest request, Category category, Restaurant restaurant);

    //exist in our database still, just remove from menu
    void removeFoodFromRestaurant(Long foodId) throws Exception;

    //for customer to filter the food type
    public List<Food> getRestaurantsFood(Long restaurantId,
                                         Boolean isVegetarian,
                                         Boolean isSeasonal,
                                         String foodCategory);

    public List<Food> searchFood(String keyword);
    public Food findFoodById(Long foodId) throws Exception;
    public Food updateAvailabilityStatus(Long foodId) throws Exception;
}
