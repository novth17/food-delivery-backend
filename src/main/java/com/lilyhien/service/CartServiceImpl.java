package com.lilyhien.service;

import com.lilyhien.model.*;
import com.lilyhien.repository.CartItemRepository;
import com.lilyhien.repository.CartRepository;
import com.lilyhien.repository.IngredientsItemRepository;
import com.lilyhien.requestDto.AddCartItemRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final UserService userService;
    private final CartItemRepository cartItemRepository;
    private final FoodService foodService;
    private final IngredientsItemRepository ingredientsItemRepository;

    @Override
    public CartItem addItemToCart(AddCartItemRequest request, String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Food food = foodService.findFoodById(request.getFoodId());
        Cart cart = cartRepository.findByCustomerId(user.getId());

        List<String> reqIngredients = new ArrayList<>(request.getIngredients());
        java.util.Collections.sort(reqIngredients);

        for (CartItem cartItem : cart.getCartItems()) {
            if (cartItem.getFood().getId().equals(food.getId())) {
                List<String> dbIngredients = new ArrayList<>(cartItem.getIngredients());
                java.util.Collections.sort(dbIngredients);
                // FOUND IT: Exact match, just update the quantity
                if (dbIngredients.equals(reqIngredients)) {
                    int newQuantity = cartItem.getQuantity() + request.getQuantity();
                    return updateCartItemQuantity(cartItem.getId(), newQuantity);
                }
            }
        }

        // Create the new CartItem with the Entity List
        CartItem newCartItem = new CartItem();
        newCartItem.setFood(food);
        newCartItem.setQuantity(request.getQuantity());
        newCartItem.setCart(cart);
        newCartItem.setIngredients(request.getIngredients());
        newCartItem.setTotalPrice(request.getQuantity() * food.getPrice()); //calculate food * amount

        //add item to the cart object
        cart.getCartItems().add(newCartItem);
        return cartItemRepository.save(newCartItem);
    }

    @Override
    public CartItem updateCartItemQuantity(Long cartItemId, int newQuantity) throws Exception {

        Optional<CartItem> optCartItem = cartItemRepository.findById(cartItemId);
        if (optCartItem.isEmpty()) {
            throw new Exception("Cart item not found with cart item id " + cartItemId);
        }

        CartItem cartItem = optCartItem.get();
        cartItem.setQuantity(newQuantity);
        cartItem.setTotalPrice(cartItem.getFood().getPrice() * newQuantity);

        return cartItemRepository.save(cartItem);
    }

    @Override
    public Cart removeItemFromCart(Long cartItemId, String jwt) throws Exception {

        User user = userService.findUserByJwtToken(jwt);
        Cart cart = cartRepository.findByCustomerId(user.getId());

        Optional<CartItem> optCartItem = cartItemRepository.findById(cartItemId);
        if (optCartItem.isEmpty()) {
            throw new Exception("Cart item not found with cart item id " + cartItemId);
        }
        CartItem cartItem = optCartItem.get();
        cart.getCartItems().remove(cartItem);
        return cartRepository.save(cart);
    }

    @Override
    public Long calculateCartTotal(Cart cart) throws Exception {
        if (cart == null || cart.getCartItems() == null) {
            return 0L;
        }
        //go one by one cart item and plus them all together
        Long total = 0L;
        for (CartItem item : cart.getCartItems()) {
            total += item.getTotalPrice();
        }
        return total;
    }

    @Override
    public Cart findCartById(Long cartId) throws Exception {
        Optional<Cart> optCart = cartRepository.findById(cartId);
        if (optCart.isEmpty()) {
            throw new Exception("Cart not found with cart  id " + cartId);
        }
        return optCart.get();
    }

    @Override
    public Cart findCartByUserJwt(String jwt) throws Exception {
       User user = userService.findUserByJwtToken(jwt);
       Cart cart = cartRepository.findByCustomerId(user.getId());
       cart.setTotal(calculateCartTotal(cart));
       return cart;
    }

    @Override
    public Cart findCartByUserId(Long userId) throws Exception {
        Cart cart = cartRepository.findByCustomerId(userId);
        cart.setTotal(calculateCartTotal(cart));
        return cart;
    }

    @Override
    public Cart clearCart(Long userId) throws Exception {
        Cart cart = findCartByUserId(userId);
        cart.getCartItems().clear();
        return cartRepository.save(cart);
    }
}
