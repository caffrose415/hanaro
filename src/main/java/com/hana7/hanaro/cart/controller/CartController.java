package com.hana7.hanaro.cart.controller;

import com.hana7.hanaro.cart.dto.CartItemRequestDto;
import com.hana7.hanaro.cart.entity.CartItem;
import com.hana7.hanaro.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/items/{memberId}")
    public ResponseEntity<CartItem> addOrUpdateCartItem(@PathVariable Long memberId, @RequestBody CartItemRequestDto requestDto) {
        try {
            CartItem cartItem = cartService.addOrUpdateCartItem(memberId, requestDto.getItemId(), requestDto.getQuantity());
            return ResponseEntity.status(HttpStatus.CREATED).body(cartItem);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<Void> removeCartItem(@PathVariable Long cartItemId) {
        cartService.removeCartItem(cartItemId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{memberId}/items")
    public ResponseEntity<List<CartItem>> getCartItems(@PathVariable Long memberId) {
        try {
            List<CartItem> cartItems = cartService.getCartItems(memberId);
            return ResponseEntity.ok(cartItems);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
