package com.lilyhien.service;

import com.lilyhien.exception.ResourceNotFoundException;
import com.lilyhien.exception.ValidationException;
import com.lilyhien.model.*;
import com.lilyhien.repository.*;
import com.lilyhien.requestDto.OrderRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class OrderServiceImpl implements OrderService{

    private final RestaurantRepository restaurantRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final CartService cartService;
    private final OrderRepository orderRepository;
    @Override
    @Transactional
    public Order createOrder(OrderRequest request, User user) throws Exception {

        // 1. Validate cart
        Cart cart = cartService.findCartByUserId(user.getId());
        if (cart == null || cart.getCartItems().isEmpty()) {
            throw new ValidationException("Cart is empty. Cannot create order.");
        }

        // 2. Find restaurant
        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + request.getRestaurantId()));

        // 3. Handle delivery address
        Address deliveryAddress = request.getDeliveryAddress();
        deliveryAddress.setUser(user);
        user.getAddresses().add(deliveryAddress);
        // Address is saved via User Cascade or by line below
        userRepository.save(user);

        // 4. Create order
        Order newOrder = new Order();
        newOrder.setCustomer(user);
        newOrder.setRestaurant(restaurant);
        newOrder.setOrderStatus(OrderStatus.PENDING);
        newOrder.setCreatedAt(LocalDateTime.now());
        newOrder.setDeliveryAddress(deliveryAddress);

        // 5. Create order items (No manual saves needed!)
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setFood(cartItem.getFood());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setTotalPrice(cartItem.getTotalPrice());
            orderItem.setIngredients(cartItem.getIngredients());

            // Link child to parent
            orderItem.setOrder(newOrder);
            orderItems.add(orderItem);
        }
        newOrder.setItems(orderItems);
        newOrder.setTotalPrice(cartService.calculateCartTotal(cart));

        // 6. The Single Save. This saves Order and all OrderItems.
        // It also links to the Restaurant via the FK in the Order table.
        Order savedOrder = orderRepository.save(newOrder);

        //7. Clear the cart after order completed
        cartService.clearCart(user.getId());

        return savedOrder;
    }

    @Override
    public Order updateOrder(Long orderId, OrderStatus newOrderStatus) throws Exception {
        Order order = findOrderById(orderId);
        if (order.getOrderStatus() == OrderStatus.DELIVERED) {
            throw new ValidationException("Order has already been delivered and cannot be modified.");
        }
        if (order.getOrderStatus() == newOrderStatus) {
            return order;
        }
        order.setOrderStatus(newOrderStatus);
        return orderRepository.save(order);
    }

    @Override
    public void cancelOrder(Long orderId) throws Exception {
        if (!orderRepository.existsById(orderId)) {
            throw new ResourceNotFoundException("Order not found with id: " + orderId);
        }
        orderRepository.deleteById(orderId);
    }

    @Override
    public List<Order> getUserOrder(Long userId) throws Exception {
        return orderRepository.findByCustomerId(userId);
    }

    //uses the Java Stream API to filter a list of object, look every order and keep only what matches orderStatus
    @Override
    public List<Order> getRestaurantOrders(Long restaurantId, OrderStatus orderStatus) throws Exception {
        List<Order> orderList = orderRepository.findByRestaurantId(restaurantId);
        if (orderStatus != null) {
            return orderList.stream()
                    .filter(order -> order.getOrderStatus() == orderStatus)
                    .toList();
        }
        return orderList;
    }

    @Override
    public Order findOrderById(Long orderId) throws Exception {
        Optional<Order> optOrder = orderRepository.findById(orderId);
        if (optOrder.isEmpty())
            throw new ResourceNotFoundException("Order not found with id " + orderId);
        return optOrder.get();
    }
}
