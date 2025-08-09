package com.hana7.hanaro.order.repository;

import com.hana7.hanaro.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
