package com.lilyhien.service;

import com.lilyhien.exception.ResourceNotFoundException;
import com.lilyhien.model.Category;
import com.lilyhien.model.Food;
import com.lilyhien.model.Restaurant;
import com.lilyhien.repository.FoodRepository;
import com.lilyhien.requestDto.CreateFoodRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FoodServiceImpl implements FoodService{

    private final FoodRepository foodRepository;

    @Override
    public Food createFood(CreateFoodRequest request, Category category, Restaurant restaurant) {
        Food food = new Food();

        food.setFoodCategory(category);
        food.setRestaurant(restaurant); //Set the relationship on the owner, prepare for DB

        food.setName(request.getName());
        food.setDescription(request.getDescription());
        food.setPrice(request.getPrice());
        food.setImages(request.getImages());

        food.setSeasonal(request.isSeasonal());
        food.setVegetarian(request.isVegetarian());
        food.setAvailable(request.isAvailable());
        food.setIngredients(request.getIngredients());
        food.setCreationDate(LocalDate.now());

        //call this the JPA provider (like Hibernate) sends an SQL INSERT to the database.
        //The database generates the ID (e.g., ID = 502)
        //if return food, the id is null -> FE cant link to this food coz no id
        //Spring Data returns the entity after it has been touched by the database. This object contains the new id = 502.
        Food savedFood = foodRepository.save(food); //update in database add a new food to Food table
        restaurant.getFoods().add(savedFood); //manually update java list inside restaurant object, both should match database
        return savedFood;
    }

    @Override
    public void removeFoodFromRestaurant(Long foodId) throws Exception {
        Food food = findFoodById(foodId);

        // Sync the Java side: remove food from the restaurant's internal list
        if (food.getRestaurant() != null) {
            food.getRestaurant().getFoods().remove(food);
        }

        // Sync the Database side: delete the record
        foodRepository.delete(food);
    }

    @Override
    public List<Food> getRestaurantsFood(Long restaurantId,
                                         Boolean isVegetarian,
                                         Boolean isSeasonal,
                                         String foodCategory) {
        List<Food> foodList = foodRepository.findByRestaurantId(restaurantId);
        /*
        * private boolean matchesVegetarian(Food f) {
               return isVegetarian == null || f.isVegetarian() == isVegetarian;
        }
        */
        //one loop checking 3 "stations"
        return foodList.stream()
                .filter(f -> isVegetarian == null || f.isVegetarian() == isVegetarian)
                .filter(f -> !Boolean.TRUE.equals(isSeasonal) || f.isSeasonal())
                .filter(f -> foodCategory == null || foodCategory.isEmpty() ||
                        (f.getFoodCategory() != null &&
                                foodCategory.equalsIgnoreCase(f.getFoodCategory().getName())))
                .collect(Collectors.toList());
    }

    @Override
    public List<Food> searchFood(String keyword) {
        return foodRepository.searchFood(keyword);
    }

    @Override
    public Food findFoodById(Long foodId) throws Exception {
        Optional<Food> optFood = foodRepository.findById(foodId);
        if (optFood.isEmpty())
            throw new ResourceNotFoundException("Food not found with food id: " + foodId);
        return optFood.get();
    }

    @Override
    public Food updateAvailabilityStatus(Long foodId) throws Exception {
        Food food = findFoodById(foodId);
        food.setAvailable(!food.isAvailable());
        return foodRepository.save(food);
    }
}
