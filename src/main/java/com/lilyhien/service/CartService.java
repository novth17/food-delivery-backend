package com.lilyhien.service;

import com.lilyhien.model.Cart;
import com.lilyhien.model.CartItem;
import com.lilyhien.requestDto.AddCartItemRequest;

public interface CartService {

    CartItem addItemToCart(AddCartItemRequest request, String jwt) throws Exception;
    CartItem updateCartItemQuantity(Long cartItemId, int newQuantity) throws Exception;
    Cart removeItemFromCart(Long cartItemId, String jwt) throws Exception;

    Long calculateCartTotal(Cart cart) throws Exception;
    Cart findCartById(Long cartId) throws Exception;
    Cart findCartByUserJwt(String jwt) throws Exception;
    Cart findCartByUserId(Long userId) throws Exception;

    Cart clearCart(Long userId)  throws Exception;
}
