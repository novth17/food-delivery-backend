package com.lilyhien.service;

import com.lilyhien.model.Cart;
import com.lilyhien.model.CartItem;
import com.lilyhien.model.Food;
import com.lilyhien.model.User;
import com.lilyhien.repository.CartItemRepository;
import com.lilyhien.repository.CartRepository;
import com.lilyhien.requestDto.AddCartItemRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CartServiceImpl implements CartService{

    private final CartRepository cartRepository;
    private final UserService userService;
    private final CartItemRepository cartItemRepository;
    private final FoodService foodService;

    @Override
    public CartItem addItemToCart(AddCartItemRequest request, String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Food food = foodService.findFoodById(request.getFoodId());
        Cart cart = cartRepository.findByCustomerId(user.getId());


        return null;
    }

    @Override
    public CartItem updateCartItemQuantity(Long CartItemId, int quantity) throws Exception {
        return null;
    }

    @Override
    public Cart removeItemFromCart(Long cartItemId, String jwt) throws Exception {
        return null;
    }

    @Override
    public Long calculateCartTotal(Cart cart) throws Exception {
        return 0L;
    }

    @Override
    public Cart findCartById(Long cartId) throws Exception {
        return null;
    }

    @Override
    public Cart findCartByUserId(Long userId) throws Exception {
        return null;
    }

    @Override
    public Cart clearCart(Long userId) throws Exception {
        return null;
    }
}
