package com.hana7.hanaro.order.repository;

import com.hana7.hanaro.order.dto.AdminOrderSummaryDTO;
import com.hana7.hanaro.order.entity.Order;
import com.hana7.hanaro.order.entity.OrderState;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>,OrderQueryRepository {
    List<Order> findByMemberId(Long memberId);
    List<Order> findByStateAndUpdatedAtBefore(OrderState state, LocalDateTime threshold);
    @EntityGraph(attributePaths = {"orderItems","orderItems.item"})
    List<Order> findByMemberIdOrderByCreatedAtDesc(Long memberId);

    @EntityGraph(attributePaths = {"orderItems","orderItems.item"})
    List<Order> findByMemberIdAndCreatedAtBetweenOrderByCreatedAtDesc(Long memberId, LocalDateTime start, LocalDateTime end);

    @EntityGraph(attributePaths = {"orderItems","orderItems.item"})
    Optional<Order> findByIdAndMemberId(Long orderId, Long memberId);

    @Modifying(clearAutomatically = true)
    @Query("""
        update Orders o
           set o.state = :next,
               o.statedAt = CURRENT_TIMESTAMP
         where o.state = :current
           and o.statedAt <= :threshold
    """)
    int updateStateBatch(@Param("current") OrderState current,
        @Param("threshold") java.time.LocalDateTime threshold,
        @Param("next") OrderState next);

}
