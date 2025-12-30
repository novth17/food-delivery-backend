package com.lilyhien.controller;

import com.lilyhien.model.Food;
import com.lilyhien.model.Restaurant;
import com.lilyhien.model.User;
import com.lilyhien.requestDto.CreateFoodRequest;
import com.lilyhien.response.MessageResponse;
import com.lilyhien.service.FoodService;
import com.lilyhien.service.RestaurantService;
import com.lilyhien.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/food")
@RequiredArgsConstructor
public class AdminFoodController {

    private final FoodService foodService;
    private final UserService userService;
    private final RestaurantService restaurantService;

    @PostMapping()
    public ResponseEntity<Food> createFood(
            @RequestBody CreateFoodRequest request,
            @RequestHeader("Authorization") String jwt)
            throws  Exception
    {
        User user = userService.findUserByJwtToken(jwt);
        Restaurant restaurant = restaurantService.getRestaurantByUserId(user.getId());
        Food food = foodService.createFood(request, request.getFoodCategory(), restaurant);
        return new ResponseEntity<>(food, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> removeFoodFromRestaurant(
            @PathVariable Long id,
            @RequestHeader("Authorization") String jwt) throws Exception
    {
        User user = userService.findUserByJwtToken(jwt);
        Restaurant restaurant = restaurantService.getRestaurantByUserId(user.getId());

        // Find the food first
        Food food = foodService.findFoodById(id);

        // Does this food belong to THIS admin's restaurant?
        if (!food.getRestaurant().getId().equals(restaurant.getId())) {
            throw new Exception("You do not have permission to delete food from another restaurant.");
        }

        foodService.removeFoodFromRestaurant(id);

        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage("Food with id " + id + " deleted successfully");
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Food> updateFoodAvailabilityStatus(
            @PathVariable Long id,
            @RequestHeader("Authorization") String jwt) throws Exception
    {
        User user = userService.findUserByJwtToken(jwt);
        Restaurant restaurant = restaurantService.getRestaurantByUserId(user.getId());
        Food food = foodService.findFoodById(id);
        // Ownership check
        if (!food.getRestaurant().getId().equals(restaurant.getId())) {
            throw new Exception("Unauthorized: This food does not belong to your restaurant.");
        }
        Food updatedFood = foodService.updateAvailabilityStatus(id);
        return new ResponseEntity<>(updatedFood, HttpStatus.OK);
    }
}
