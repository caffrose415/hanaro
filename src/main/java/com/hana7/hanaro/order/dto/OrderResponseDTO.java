package com.hana7.hanaro.order.dto;

import com.hana7.hanaro.order.entity.Order;
import com.hana7.hanaro.order.entity.OrderItem;
import com.hana7.hanaro.order.entity.OrderState;

import java.util.List;

public record OrderResponseDTO(
	OrderState state,
	int totalQuantity,
	int totalPrice,
	List<Line> items
) {
	public record Line( String name, int price, int cnt, int subtotal) {}

	public static OrderResponseDTO from(Order o) {
		List<Line> lines = o.getOrderItems().stream()
			.map(oi -> new Line(
				oi.getItem().getName(),
				oi.getPrice(),
				oi.getCnt(),
				oi.getPrice() * oi.getCnt()))
			.toList();
		int q = lines.stream().mapToInt(Line::cnt).sum();
		int p = lines.stream().mapToInt(Line::subtotal).sum();
		return new OrderResponseDTO( o.getState(), q, p, lines);
	}
}
