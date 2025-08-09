package com.hana7.hanaro.order.repository;

import com.hana7.hanaro.RepositoryTest;
import com.hana7.hanaro.item.entity.Item;
import com.hana7.hanaro.item.repository.ItemRepository;
import com.hana7.hanaro.member.entity.Auth;
import com.hana7.hanaro.member.entity.Member;
import com.hana7.hanaro.member.repository.MemberRepository;
import com.hana7.hanaro.order.entity.Order;
import com.hana7.hanaro.order.entity.OrderItem;
import com.hana7.hanaro.order.entity.OrderState;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class OrderRepositoryTest extends RepositoryTest {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ItemRepository itemRepository;

    private Order testOrder;
    private Member testMember;
    private Item testItem;

    @BeforeEach
    void setup() {
        testMember = Member.builder()
                .email("orderuser@hanaro.com")
                .password("password123")
                .nickname("orderuser")
                .auth(Auth.USERS)
                .build();
        memberRepository.save(testMember);

        testItem = Item.builder()
                .name("주문테스트상품")
                .price(20000)
                .stock(100)
                .build();
        itemRepository.save(testItem);

        Order order = Order.builder()
                .member(testMember)
                .state(OrderState.PAYED)
                .build();

        OrderItem orderItem = OrderItem.builder()
                .orders(order)
                .item(testItem)
                .price(testItem.getPrice())
                .cnt(3)
                .build();
        order.getOrderItems().add(orderItem);

        testOrder = orderRepository.save(order);
    }

    @Test
    void createOrderTest() {
        Order order = Order.builder()
            .member(testMember)
            .state(OrderState.PAYED)
            .build();
        order.getOrderItems().add(
            OrderItem.builder().orders(order).item(testItem).price(testItem.getPrice()).cnt(2).build()
        );
        Order saved = orderRepository.save(order);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getOrderItems()).hasSize(1);
    }

    @Test
    void findOrderTest() {
        Order foundOrder = orderRepository.findById(testOrder.getId()).orElseThrow();

        assertThat(foundOrder.getMember().getId()).isEqualTo(testMember.getId());
        assertThat(foundOrder.getState()).isEqualTo(OrderState.PAYED);
        assertThat(foundOrder.getOrderItems()).hasSize(1);
        assertThat(foundOrder.getOrderItems().getFirst().getItem().getId()).isEqualTo(testItem.getId());
    }

    @Test
    void updateOrderStateTest() {
        Order orderToUpdate = orderRepository.findById(testOrder.getId()).orElseThrow();
        orderToUpdate.setState(OrderState.DELIVERED);
        orderRepository.save(orderToUpdate);

        Order foundOrder = orderRepository.findById(testOrder.getId()).orElseThrow();
        assertThat(foundOrder.getState()).isEqualTo(OrderState.DELIVERED);
    }

    @Test
    void deleteOrderTest() {
        orderRepository.deleteById(testOrder.getId());

        assertThat(orderRepository.findById(testOrder.getId())).isEmpty();
    }
}
