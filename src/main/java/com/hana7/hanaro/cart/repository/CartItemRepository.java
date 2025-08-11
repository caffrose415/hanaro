package com.hana7.hanaro.cart.repository;

import java.util.Optional;

import com.hana7.hanaro.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
	Optional<CartItem> findByCartIdAndItemId(Long cartId, Long itemId);
	long deleteByCartId(Long cartId);
}
