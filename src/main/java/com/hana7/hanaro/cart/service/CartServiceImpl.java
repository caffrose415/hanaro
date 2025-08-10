package com.hana7.hanaro.cart.service;

import com.hana7.hanaro.cart.entity.Cart;
import com.hana7.hanaro.cart.entity.CartItem;
import com.hana7.hanaro.cart.repository.CartItemRepository;
import com.hana7.hanaro.cart.repository.CartRepository;
import com.hana7.hanaro.item.entity.Item;
import com.hana7.hanaro.item.repository.ItemRepository;
import com.hana7.hanaro.member.entity.Member;
import com.hana7.hanaro.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

    @Override
    public CartItem addOrUpdateCartItem(Long memberId, Long itemId, int quantity) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        Cart cart = member.getCart();
        if (cart == null) {
            throw new IllegalStateException("Cart not found for member: " + memberId);
        }

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));

        Optional<CartItem> existingCartItem = cart.getCartItems().stream()
                .filter(ci -> ci.getItem().getId().equals(itemId))
                .findFirst();

        CartItem cartItem;
        if (existingCartItem.isPresent()) {
            cartItem = existingCartItem.get();
            cartItem.setCnt(cartItem.getCnt() + quantity);
        } else {
            cartItem = CartItem.builder()
                    .cart(cart)
                    .item(item)
                    .cnt(quantity)
                    .price(item.getPrice()) // Assuming price is taken from item at the time of adding to cart
                    .build();
            cart.getCartItems().add(cartItem); // Add to cart's collection
        }

        return cartItemRepository.save(cartItem);
    }

    @Override
    public void removeCartItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    @Override
    public List<CartItem> getCartItems(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        Cart cart = member.getCart();
        if (cart == null) {
            throw new IllegalStateException("Cart not found for member: " + memberId);
        }
        return cart.getCartItems();
    }
}
