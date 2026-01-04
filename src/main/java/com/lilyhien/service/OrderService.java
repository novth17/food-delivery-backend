package com.lilyhien.service;

import com.lilyhien.model.Order;
import com.lilyhien.model.OrderStatus;
import com.lilyhien.model.User;
import com.lilyhien.requestDto.OrderRequest;

import java.util.List;

public interface OrderService {
    Order createOrder(OrderRequest request, User user) throws Exception;
    Order updateOrder(Long orderId, OrderStatus newOrderStatus) throws Exception;
    void cancelOrder(Long orderId) throws Exception;
    List<Order> getUserOrder(Long userId) throws Exception;
    List<Order> getRestaurantOrders(Long restaurantId, OrderStatus orderStatus) throws Exception;
    Order findOrderById(Long orderId) throws Exception;
}
