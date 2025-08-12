package com.hana7.hanaro.cart.repository;

import java.util.Optional;

import com.hana7.hanaro.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
	Optional<Cart> findByMemberId(Long memberId);
}
