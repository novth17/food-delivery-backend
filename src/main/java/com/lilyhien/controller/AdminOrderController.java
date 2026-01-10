package com.lilyhien.controller;


import com.lilyhien.exception.UnauthorizedException;
import com.lilyhien.exception.ValidationException;
import com.lilyhien.model.Order;
import com.lilyhien.model.OrderStatus;
import com.lilyhien.model.Restaurant;
import com.lilyhien.model.User;
import com.lilyhien.service.OrderService;
import com.lilyhien.service.RestaurantService;
import com.lilyhien.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin")
public class AdminOrderController {

    private final OrderService orderService;
    private final UserService userService;
    private final RestaurantService restaurantService;

    @GetMapping("/order/restaurant")
    public ResponseEntity<List<Order>> getOrderHistory(
            @RequestHeader("Authorization") String jwt,
            @RequestParam(required = false) String orderStatus
            ) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Restaurant restaurant = restaurantService.findRestaurantByUserId(user.getId());

        //check null to avoid crash with a NullPointerException when calling statusString.toUpperCase().
        OrderStatus statusEnum = (orderStatus != null) ? convertToEnum(orderStatus) : null;

        List<Order> orderList = orderService.getRestaurantOrders(restaurant.getId(), statusEnum);

        return new ResponseEntity<>(orderList, HttpStatus.OK);
    }

    @PutMapping("/order/restaurant/{orderId}/{orderStatus}")
    public ResponseEntity<Order> updateOrderStatus(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long orderId,
            @PathVariable String orderStatus
    ) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Restaurant restaurant = restaurantService.findRestaurantByUserId(user.getId());

        Order toVerifyOrder = orderService.findOrderById(orderId);
        if (!toVerifyOrder.getRestaurant().getId().equals(restaurant.getId()))
            throw new UnauthorizedException("You do not have permission to update an order for another restaurant.");

        Order order = orderService.updateOrder(orderId, convertToEnum(orderStatus));
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    private OrderStatus convertToEnum(String statusString) throws Exception {
        try {
            return OrderStatus.valueOf(statusString.toUpperCase().trim());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new ValidationException("Invalid status: " + statusString + ". Please use a valid OrderStatus.");
        }
    }
}
