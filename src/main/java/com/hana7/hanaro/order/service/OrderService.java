package com.hana7.hanaro.order.service;

import com.hana7.hanaro.order.entity.Order;

import java.util.List;

public interface OrderService {
    Order createOrderFromCart(Long memberId);
    List<Order> getOrdersByMemberId(Long memberId);
}
