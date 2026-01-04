package com.lilyhien.service;

import com.lilyhien.model.*;
import com.lilyhien.repository.*;
import com.lilyhien.requestDto.OrderRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
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
            throw new Exception("Cart is empty. Cannot create order.");
        }

        // 2. Find restaurant
        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(() -> new Exception("Restaurant not found with id: " + request.getRestaurantId()));

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

        // 8. Clear the cart - will do inb controller
       // cartService.clearCart(user.getId());

        return savedOrder;
    }

    @Override
    public Order updateOrder(Long orderId, String orderStatus) throws Exception {
        return null;
    }

    @Override
    public void cancelOrder(Long orderId) throws Exception {

    }

    @Override
    public List<Order> getUserOrder(Long userId) throws Exception {
        return List.of();
    }

    @Override
    public List<Order> getRestaurantOrders(Long restaurantId, String orderStatus) throws Exception {
        return List.of();
    }
}
