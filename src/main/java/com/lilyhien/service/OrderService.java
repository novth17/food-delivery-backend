package com.lilyhien.service;

import com.lilyhien.model.Order;
import com.lilyhien.model.User;
import com.lilyhien.requestDto.OrderRequest;

import java.util.List;

public interface OrderService {
    Order createOrder(OrderRequest request, User user) throws Exception;
    Order updateOrder(Long orderId, String orderStatus) throws Exception;
    void cancelOrder(Long orderId) throws Exception;
    List<Order> getUserOrder(Long userId) throws Exception;
    List<Order> getRestaurantOrders(Long restaurantId, String orderStatus) throws Exception;
}
