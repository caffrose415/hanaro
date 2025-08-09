package com.hana7.hanaro.cart.repository;

import com.hana7.hanaro.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
