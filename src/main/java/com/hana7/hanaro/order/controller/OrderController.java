package com.hana7.hanaro.order.controller;

import com.hana7.hanaro.order.entity.Order;
import com.hana7.hanaro.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/{memberId}/fromCart")
    public ResponseEntity<Order> createOrderFromCart(@PathVariable Long memberId) {
        try {
            Order order = orderService.createOrderFromCart(memberId);
            return ResponseEntity.status(HttpStatus.CREATED).body(order);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/{memberId}/history")
    public ResponseEntity<List<Order>> getOrderHistory(@PathVariable Long memberId) {
        try {
            List<Order> orders = orderService.getOrdersByMemberId(memberId);
            return ResponseEntity.ok(orders);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
