package com.lilyhien.controller;

import com.lilyhien.model.Restaurant;
import com.lilyhien.model.User;
import com.lilyhien.requestDto.CreateRestaurantRequest;
import com.lilyhien.response.MessageResponse;
import com.lilyhien.service.RestaurantService;
import com.lilyhien.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
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
    private ResponseEntity<Restaurant> createRestaurant (
            @RequestBody CreateRestaurantRequest request,
            @RequestHeader("Authorization") String jwt)
            throws Exception
    {
        User user = userService.findUserByJwtToken(jwt);
        Restaurant restaurant = restaurantService.createRestaurant(request, user);
        return new ResponseEntity<>(restaurant, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    private ResponseEntity<Restaurant> updateRestaurant (
            @RequestBody CreateRestaurantRequest request,
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long id)
            throws Exception
    {
        User user = userService.findUserByJwtToken(jwt);
        Restaurant restaurant = restaurantService.updateRestaurant(id, request);
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<MessageResponse> deleteRestaurant (
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long id)
            throws Exception
    {
        User user = userService.findUserByJwtToken(jwt);
        restaurantService.deleteRestaurant(id);

        MessageResponse response = new MessageResponse();
        response.setMessage("Restaurant deleted successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}/status")
    private ResponseEntity<Restaurant> updateRestaurantStatus (
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long id)
            throws Exception
    {
        User user = userService.findUserByJwtToken(jwt);
        Restaurant restaurant = restaurantService.updateRestaurantStatus(id);
        return new ResponseEntity<>(restaurant, HttpStatus.OK);

    }

    @GetMapping("/user")
    private ResponseEntity<Restaurant> findRestaurantByUserId (
            @RequestHeader("Authorization") String jwt)
            throws Exception
    {
        User user = userService.findUserByJwtToken(jwt);
        Restaurant restaurant = restaurantService.getRestaurantByUserId(user.getId());
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }
}
