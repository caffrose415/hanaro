package com.hana7.hanaro.order.dto;

import com.hana7.hanaro.order.entity.Order;
import com.hana7.hanaro.order.entity.OrderState;

import java.time.LocalDateTime;
import java.util.List;

public record AdminOrderDetailDTO(
	Long memberId,
	String nickname,
	OrderState state,
	LocalDateTime createdAt,
	int totalQuantity,
	int totalPrice,
	List<Line> items
) {
	public record Line(Long itemId, String name, int price, int cnt, int subtotal) {}

	public static AdminOrderDetailDTO from(Order o) {
		List<Line> lines = o.getOrderItems().stream()
			.map(oi -> new Line(
				oi.getItem().getId(),
				oi.getItem().getName(),
				oi.getPrice(),
				oi.getCnt(),
				oi.getPrice() * oi.getCnt()
			)).toList();
		int q = lines.stream().mapToInt(Line::cnt).sum();
		int p = lines.stream().mapToInt(Line::subtotal).sum();
		return new AdminOrderDetailDTO(
			 o.getMember().getId(), o.getMember().getNickname(),
			o.getState(), o.getCreatedAt(), q, p, lines
		);
	}
}
