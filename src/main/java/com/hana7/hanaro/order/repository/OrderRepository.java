package com.hana7.hanaro.order.repository;

import com.hana7.hanaro.order.entity.Order;
import com.hana7.hanaro.order.entity.OrderItem;
import com.hana7.hanaro.order.entity.OrderState;
import com.hana7.hanaro.stat.entity.SaleStat;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>,OrderQueryRepository {
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


    @Query(value = "select 'today' saledt, count(*) ordercnt, 0 totamt from Orders o"
        + " where o.createdAt between concat(:saledt, ' 00:00:00.00') and concat(:saledt, ' 23:59:59.99')", nativeQuery = true)
    public SaleStat getTodayStat(@Param("saledt") String saledt);

    @Query(value =
        "select oi.item as id, max(oi.id) as orders, oi.item, sum(oi.cnt) as cnt, sum(oi.cnt * oi.price) as price"
            + "  from Orders o inner join OrderItem oi on o.id = oi.orders"
            + " where o.createdAt between concat(:saledt, ' 00:00:00.00') and concat(:saledt, ' 23:59:59.99')"
            + " group by oi.item", nativeQuery = true)
    public List<OrderItem> getTodayItemStat(@Param("saledt") String saledt);

}
