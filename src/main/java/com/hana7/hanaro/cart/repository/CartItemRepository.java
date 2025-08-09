package com.hana7.hanaro.cart.repository;

import com.hana7.hanaro.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
