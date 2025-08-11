package com.hana7.hanaro.cart.service;

import org.springframework.security.core.Authentication;

import com.hana7.hanaro.cart.dto.CartAddRequestDTO;
import com.hana7.hanaro.cart.dto.CartResponseDTO;
import com.hana7.hanaro.cart.dto.CartUpdateRequestDTO;

public interface CartService {
	CartResponseDTO getMyCart(Authentication auth);
	CartResponseDTO addItem(Authentication auth, CartAddRequestDTO req);
	CartResponseDTO updateItem(Authentication auth, Long cartItemId, CartUpdateRequestDTO req);
	void removeItem(Authentication auth, Long cartItemId);
}
