package com.hana7.hanaro.cart.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hana7.hanaro.cart.dto.CartAddRequestDTO;
import com.hana7.hanaro.cart.dto.CartResponseDTO;
import com.hana7.hanaro.cart.dto.CartUpdateRequestDTO;
import com.hana7.hanaro.cart.service.CartService;
import com.hana7.hanaro.order.dto.OrderResponseDTO;
import com.hana7.hanaro.order.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user/cart")
@Tag(name = "[사용자] 장바구니")
@RequiredArgsConstructor
public class CartController {

	private final CartService cartService;
	private final OrderService orderService;

	@PreAuthorize("hasRole('USERS')")
	@GetMapping
	@Operation(summary = "장바구니 조회")
	public ResponseEntity<CartResponseDTO> getCart(Authentication auth) {
		return ResponseEntity.ok(cartService.getMyCart(auth));
	}

	@PreAuthorize("hasRole('USERS')")
	@PostMapping("/items")
	@Operation(summary = "장바구니 담기")
	public ResponseEntity<CartResponseDTO> addCart(@Valid @RequestBody CartAddRequestDTO req,Authentication auth) {
		return ResponseEntity.status(HttpStatus.CREATED).body(cartService.addItem(auth,req));
	}

	@PreAuthorize("hasRole('USERS')")
	@PatchMapping("/items/{cartItemId}")
	@Operation(summary = "장바구니 수량 수정")
	public ResponseEntity<CartResponseDTO> updateCart(@PathVariable Long cartItemId,
		@Valid @RequestBody CartUpdateRequestDTO req, Authentication auth) {
		return ResponseEntity.ok(cartService.updateItem(auth,cartItemId,req));
	}

	@PreAuthorize("hasRole('USERS')")
	@DeleteMapping("/items/{cartItemId}")
	@Operation(summary = "장바구니 항목 삭제")
	public ResponseEntity<Void> removeCart(@PathVariable Long cartItemId,Authentication auth) {
		cartService.removeItem(auth,cartItemId);
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("hasRole('USERS')")
	@DeleteMapping
	@Operation(summary = "장바구니 구매하기")
	public ResponseEntity<OrderResponseDTO> clearCart(Authentication auth) {
		var res = orderService.checkout(auth);
		return ResponseEntity.status(HttpStatus.CREATED).body(res);
	}

}
