package com.hana7.hanaro.cart.service;

import com.hana7.hanaro.cart.entity.CartItem;

import java.util.List;

public interface CartService {
    CartItem addOrUpdateCartItem(Long memberId, Long itemId, int quantity);
    void removeCartItem(Long cartItemId);
    List<CartItem> getCartItems(Long memberId); // To view cart contents
}
