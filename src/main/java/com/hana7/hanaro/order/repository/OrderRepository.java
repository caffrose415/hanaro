package com.hana7.hanaro.order.repository;

import com.hana7.hanaro.order.dto.AdminOrderSummaryDTO;
import com.hana7.hanaro.order.entity.Order;
import com.hana7.hanaro.order.entity.OrderState;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>,OrderQueryRepository {
    List<Order> findByMemberId(Long memberId);
    List<Order> findByStateAndUpdatedAtBefore(OrderState state, LocalDateTime threshold);
}
