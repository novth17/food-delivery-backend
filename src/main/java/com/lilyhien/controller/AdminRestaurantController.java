package com.lilyhien.controller;

import com.lilyhien.model.Restaurant;
import com.lilyhien.model.User;
import com.lilyhien.requestDto.CreateRestaurantRequest;
import com.lilyhien.response.MessageResponse;
import com.lilyhien.service.RestaurantService;
import com.lilyhien.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/restaurants")
public class AdminRestaurantController {

    private final UserService userService;
    private final RestaurantService restaurantService;

    @PostMapping()
    public ResponseEntity<Restaurant> createRestaurant(
            @RequestBody CreateRestaurantRequest request,
            @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Restaurant restaurant = restaurantService.createRestaurant(request, user);
        return new ResponseEntity<>(restaurant, HttpStatus.CREATED);
    }

    // Identify restaurant by Token.
    @PutMapping()
    public ResponseEntity<Restaurant> updateRestaurant(
            @RequestBody CreateRestaurantRequest request,
            @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);

        // Security Check: Find the restaurant owned by THIS user
        Restaurant restaurant = restaurantService.findRestaurantByUserId(user.getId());

        // Pass the trusted ID to the service
        Restaurant updatedRestaurant = restaurantService.updateRestaurant(restaurant.getId(), request);
        return new ResponseEntity<>(updatedRestaurant, HttpStatus.OK);
    }

    // Identify restaurant by Token.
    @DeleteMapping()
    public ResponseEntity<MessageResponse> deleteRestaurant(
            @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);

        // Security Check: Find the restaurant owned by THIS user
        Restaurant restaurant = restaurantService.findRestaurantByUserId(user.getId());

        restaurantService.deleteRestaurant(restaurant.getId());

        MessageResponse response = new MessageResponse();
        response.setMessage("Restaurant deleted successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Identify restaurant by Token.
    @PutMapping("/status")
    public ResponseEntity<Restaurant> updateRestaurantStatus(
            @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Restaurant restaurant = restaurantService.findRestaurantByUserId(user.getId());

        Restaurant updatedRestaurant = restaurantService.updateRestaurantStatus(restaurant.getId());
        return new ResponseEntity<>(updatedRestaurant, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<Restaurant> findRestaurantByUserId(
            @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Restaurant restaurant = restaurantService.findRestaurantByUserId(user.getId());
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }
}