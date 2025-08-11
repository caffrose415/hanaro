package com.hana7.hanaro.cart.dto;

import java.util.List;

import com.hana7.hanaro.cart.entity.Cart;

public record CartResponseDTO(
	List<CartItemResponseDTO> items,
	int totalQuentity,
	int totalPrice
) {
	public static CartResponseDTO from(Cart cart) {
		var items = cart.getCartItems().stream()
			.map(CartItemResponseDTO::from).toList();
		int q = items.stream().mapToInt(CartItemResponseDTO::cnt).sum();
		int p = items.stream().mapToInt(CartItemResponseDTO::subTotal).sum();
		return new CartResponseDTO(items, q, p);
	}
}
