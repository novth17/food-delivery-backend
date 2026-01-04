package com.lilyhien.controller;

import com.lilyhien.model.Category;
import com.lilyhien.model.Restaurant;
import com.lilyhien.model.User;
import com.lilyhien.service.CategoryService;
import com.lilyhien.service.RestaurantService;
import com.lilyhien.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// @Controller: Used for Traditional Web Pages. It returns a "View" (like an HTML file).
    //return "hello"; // Spring looks for hello.html
// @RestController: Used for REST APIs. It returns Data (JSON or XML).
    //return new Food("Pizza", 10L); // Returns {"name": "Pizza", "price": 10}

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class CategoryController {
    private final CategoryService categoryService;
    private final RestaurantService restaurantService;
    private final UserService userService;

    @PostMapping("/admin/category")
    public ResponseEntity<Category> createCategory(
            @RequestBody Category category,
            @RequestHeader("Authorization") String jwt) throws Exception
    {
        User user = userService.findUserByJwtToken(jwt);
        Category newCategory = categoryService.createCategory(category.getName(), user.getId());
        return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
    }

    @GetMapping("/category/restaurant")
    public ResponseEntity<List<Category>> getRestaurantCategory(
            @RequestHeader("Authorization") String jwt) throws Exception
    {
        User user = userService.findUserByJwtToken(jwt);

        Restaurant restaurant = restaurantService.findRestaurantByUserId(user.getId());
        List<Category> newCategoryList = categoryService.findCategoryByRestaurantId(restaurant.getId());
        return new ResponseEntity<>(newCategoryList, HttpStatus.OK);
    }
}
