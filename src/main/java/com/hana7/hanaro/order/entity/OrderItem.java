package com.hana7.hanaro.order.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.hana7.hanaro.item.entity.Item;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders",
        foreignKey = @ForeignKey(name = "fk_OrderItem_Order"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Order orders;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item",
        foreignKey = @ForeignKey(name = "fk_OrderItem_Item"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Item item;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int cnt;
}
