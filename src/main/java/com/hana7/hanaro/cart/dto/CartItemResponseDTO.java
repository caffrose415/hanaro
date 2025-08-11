package com.hana7.hanaro.cart.dto;

import com.hana7.hanaro.cart.entity.CartItem;

public record CartItemResponseDTO(
	String itemName,

	int unitPrice,

	int cnt,

	int subTotal

) {
	public static CartItemResponseDTO from(CartItem ci) {
		return new CartItemResponseDTO(
			ci.getItem().getName(),
			ci.getPrice(),
			ci.getCnt(),
			ci.getPrice() * ci.getCnt()
		);
	}
}
