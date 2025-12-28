package com.lilyhien.controller;

import com.lilyhien.model.Food;
import com.lilyhien.model.User;
import com.lilyhien.repository.FoodRepository;
import com.lilyhien.repository.RestaurantRepository;
import com.lilyhien.service.FoodService;
import com.lilyhien.service.RestaurantService;
import com.lilyhien.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/food")
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;
    private final UserService userService;

    @GetMapping("/search")
    public ResponseEntity<List<Food>> searchFood(
            @RequestHeader("Authorization") String jwt,
            @RequestParam String keyword)
            throws  Exception
    {
        User user = userService.findUserByJwtToken(jwt);
        List<Food> foodList = foodService.searchFood(keyword);
        return new ResponseEntity<>(foodList, HttpStatus.OK);
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<Food>> getRestaurantsFood(
            @RequestParam Boolean isVegetarian,
            @RequestParam Boolean isSeasonal,
            @RequestParam(required = false) String foodCategory,
            @PathVariable Long restaurantId,
            @RequestHeader("Authorization") String jwt)
            throws  Exception
    {
        User user = userService.findUserByJwtToken(jwt);
        List<Food> foodList = foodService.getRestaurantsFood(restaurantId, isVegetarian, isSeasonal, foodCategory);
        return new ResponseEntity<>(foodList, HttpStatus.OK);
    }


}
