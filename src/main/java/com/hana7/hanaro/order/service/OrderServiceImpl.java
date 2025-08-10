package com.hana7.hanaro.order.service;

import com.hana7.hanaro.cart.entity.Cart;
import com.hana7.hanaro.cart.entity.CartItem;
import com.hana7.hanaro.cart.repository.CartItemRepository;
import com.hana7.hanaro.item.entity.Item;
import com.hana7.hanaro.item.repository.ItemRepository;
import com.hana7.hanaro.member.entity.Member;
import com.hana7.hanaro.member.repository.MemberRepository;
import com.hana7.hanaro.order.entity.Order;
import com.hana7.hanaro.order.entity.OrderItem;
import com.hana7.hanaro.order.entity.OrderState;
import com.hana7.hanaro.order.repository.OrderItemRepository;
import com.hana7.hanaro.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public Order createOrderFromCart(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        Cart cart = member.getCart();
        if (cart == null || cart.getCartItems().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty or not found for member: " + memberId);
        }

        Order order = Order.builder()
                .member(member)
                .state(OrderState.PAYED)
                .orderItems(new ArrayList<>())
                .build();
        orderRepository.save(order);

        for (CartItem cartItem : cart.getCartItems()) {
            Item item = cartItem.getItem();
            if (item.getStock() < cartItem.getCnt()) {
                throw new IllegalArgumentException("Not enough stock for item: " + item.getName());
            }

            OrderItem orderItem = OrderItem.builder()
                    .orders(order)
                    .item(item)
                    .price(cartItem.getPrice())
                    .cnt(cartItem.getCnt())
                    .build();
            orderItemRepository.save(orderItem);
            order.getOrderItems().add(orderItem);

            item.setStock(item.getStock() - cartItem.getCnt());
            itemRepository.save(item);
        }

        // Clear the cart
        cartItemRepository.deleteAll(cart.getCartItems());
        cart.getCartItems().clear(); // Clear the collection in memory as well

        return order;
    }

    @Override
    public List<Order> getOrdersByMemberId(Long memberId) {
        return orderRepository.findByMemberId(memberId);
    }
}