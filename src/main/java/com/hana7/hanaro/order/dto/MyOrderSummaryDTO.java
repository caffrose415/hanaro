package com.hana7.hanaro.order.dto;

import com.hana7.hanaro.order.entity.Order;
import com.hana7.hanaro.order.entity.OrderItem;
import com.hana7.hanaro.order.entity.OrderState;
import java.time.LocalDateTime;

public record MyOrderSummaryDTO(
	OrderState state,
	LocalDateTime createdAt,
	int totalQuantity,
	int totalPrice
) {
	public static MyOrderSummaryDTO from(Order o) {
		int q = o.getOrderItems().stream().mapToInt(OrderItem::getCnt).sum();
		int p = o.getOrderItems().stream().mapToInt(oi -> oi.getCnt() * oi.getPrice()).sum();
		return new MyOrderSummaryDTO(o.getState(), o.getCreatedAt(), q, p);
	}
}
