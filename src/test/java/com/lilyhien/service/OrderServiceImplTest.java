package com.lilyhien.service;

import com.lilyhien.exception.ResourceNotFoundException;
import com.lilyhien.exception.ValidationException;
import com.lilyhien.model.*;
import com.lilyhien.repository.OrderRepository;
import com.lilyhien.repository.RestaurantRepository;
import com.lilyhien.repository.UserRepository;
import com.lilyhien.requestDto.OrderRequest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock private OrderRepository orderRepository;
    @Mock private CartService cartService;
    @Mock private RestaurantRepository restaurantRepository; // Added this
    @Mock private UserRepository userRepository;             // Added this

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void findOrderById_NotFound_ThrowsException() {
        // Arrange
        Long orderId = 999L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            orderService.findOrderById(orderId);
        });
    }

    @Test
    void createOrder_EmptyCart_ThrowsException() throws Exception {
        // Arrange
        User user = new User();
        user.setId(1L);

        Cart emptyCart = new Cart();
        emptyCart.setCartItems(new ArrayList<>()); // Empty list!

        when(cartService.findCartByUserId(user.getId())).thenReturn(emptyCart);

        // Act & Assert
        assertThrows(ValidationException.class, () -> {
            orderService.createOrder(new OrderRequest(), user);
        });

        // Verify that we stopped early and NEVER tried to save an order
        verify(orderRepository, never()).save(any());
    }

    @Test
    void createOrder_Success() throws Exception {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setAddresses(new ArrayList<>());

        OrderRequest request = new OrderRequest();
        request.setRestaurantId(5L);
        Address deliveryAddr = new Address();

        deliveryAddr.setStreetAddress("123 Street");
        request.setDeliveryAddress(deliveryAddr);

        // Mock Cart with items so it doesn't throw ValidationException
        Cart mockCart = new Cart();
        CartItem item = new CartItem();
        item.setTotalPrice(100L);
        mockCart.setCartItems(List.of(item));

        Restaurant mockRestaurant = new Restaurant();
        mockRestaurant.setId(5L);

        Order savedOrder = new Order();
        savedOrder.setId(100L);
        savedOrder.setDeliveryAddress(deliveryAddr);

        // Define Mock Behaviors
        when(cartService.findCartByUserId(user.getId())).thenReturn(mockCart);
        when(restaurantRepository.findById(5L)).thenReturn(Optional.of(mockRestaurant));
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
        when(cartService.calculateCartTotal(any())).thenReturn(100L);

        // Act
        Order actualOrder = orderService.createOrder(request, user);

        // Assert
        assertNotNull(actualOrder);
        assertEquals(100L, actualOrder.getId());
        // use field that actually holds the street name in Address model
        assertEquals("123 Street", actualOrder.getDeliveryAddress().getStreetAddress());

        // Verify all side effects occurred
        verify(userRepository).save(user);
        verify(orderRepository).save(any(Order.class));
        verify(cartService).clearCart(user.getId());
    }


    @Test
    void updateOrder_Success() throws Exception {
        // Arrange
        Long orderId = 1L;
        Order existingOrder = new Order();
        existingOrder.setId(orderId);
        existingOrder.setOrderStatus(OrderStatus.PENDING);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Order updatedOrder = orderService.updateOrder(orderId, OrderStatus.DELIVERED);

        // Assert
        assertEquals(OrderStatus.DELIVERED, updatedOrder.getOrderStatus());
        verify(orderRepository).save(existingOrder);
    }

    @Test
    void updateOrder_AlreadyDelivered_ThrowsException() {
        // Arrange
        Long orderId = 1L;
        Order deliveredOrder = new Order();
        deliveredOrder.setOrderStatus(OrderStatus.DELIVERED);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(deliveredOrder));

        // Act & Assert
        assertThrows(ValidationException.class, () -> {
            orderService.updateOrder(orderId, OrderStatus.PENDING);
        });
    }

    @Test
    void cancelOrder_Success() throws Exception {
        // Arrange
        Long orderId = 1L;
        when(orderRepository.existsById(orderId)).thenReturn(true);

        // Act
        orderService.cancelOrder(orderId);

        // Assert
        verify(orderRepository, times(1)).deleteById(orderId);
    }

    @Test
    void getUserOrder_Success() throws Exception {
        // 1. Arrange
        Long userId = 1L;
        List<Order> mockOrders = new ArrayList<>();
        Order order1 = new Order();
        order1.setId(101L);
        Order order2 = new Order();
        order2.setId(102L);
        mockOrders.add(order1);
        mockOrders.add(order2);

        // Mock the repository to return our list when called with this userId
        when(orderRepository.findByCustomerId(userId)).thenReturn(mockOrders);

        // Act
        List<Order> actualOrders = orderService.getUserOrder(userId);

        // Assert
        assertNotNull(actualOrders);
        assertEquals(2, actualOrders.size(), "Should return 2 orders for this user");
        assertEquals(101L, actualOrders.get(0).getId());

        // Verify interaction
        verify(orderRepository, times(1)).findByCustomerId(userId);
    }

    @Test
    void getRestaurantOrders_FiltersByStatus() throws Exception {
        // arrange
        Long resId = 1L;
        Order o1 = new Order(); o1.setOrderStatus(OrderStatus.PENDING);
        Order o2 = new Order(); o2.setOrderStatus(OrderStatus.DELIVERED);

        when(orderRepository.findByRestaurantId(resId)).thenReturn(List.of(o1, o2));

        // act
        List<Order> results = orderService.getRestaurantOrders(resId, OrderStatus.PENDING);

        // assert
        assertEquals(1, results.size());
        assertEquals(OrderStatus.PENDING, results.get(0).getOrderStatus());
    }

    @Test
    void findOrderById() throws Exception {

        //arrange
        Long orderId = 1L;
        Order mockOrder = new Order();
        mockOrder.setId(orderId);
        mockOrder.setOrderStatus(OrderStatus.PREPARING);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockOrder));

        //act
        Order result = orderService.findOrderById(orderId);

        //assert
        assertNotNull(result);
        assertEquals(orderId, result.getId());
        assertEquals(OrderStatus.PREPARING, result.getOrderStatus());
    }
}