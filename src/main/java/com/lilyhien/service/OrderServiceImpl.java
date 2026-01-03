package com.lilyhien.service;

import com.lilyhien.model.*;
import com.lilyhien.repository.*;
import com.lilyhien.requestDto.OrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService{

    private final RestaurantRepository restaurantRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final CartService cartService;
    private final UserService userService;


    @Override
    public Order createOrder(OrderRequest request, User user) throws Exception {
        Address deliveryAddress = request.getDeliveryAddress();
        Address savedAddresses = addressRepository.save(deliveryAddress);

        //if user account don't have this address then save it to database
        if (!user.getAddresses().contains(savedAddresses)) {
            user.getAddresses().add(savedAddresses);
            userRepository.save(user);
        }
        Restaurant restaurant = restaurantRepository.findRestaurantsById(request.getRestaurantId());

        Order order = new Order();
        order.setCustomer(user);
        order.setCreatedAt(new Date());
        order.setOrderStatus("PENDING");
        order.setDeliveryAddress(savedAddresses);

        order.setRestaurant(restaurant);


        Cart cart = cartService.findCartByUserId(user.getId());

        List <OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setFood(cartItem.getFood());
            orderItem.setIngredients(cartItem.getIngredients());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setTotalPrice(cartItem.getTotalPrice());

            OrderItem savedItem = orderItemRepository.save(orderItem);
            orderItems.add(savedItem);
        }

        return ;
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
