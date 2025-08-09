package com.hana7.hanaro.order.repository;

import com.hana7.hanaro.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
