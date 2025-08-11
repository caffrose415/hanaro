package com.hana7.hanaro.order.dto;

import com.hana7.hanaro.order.entity.OrderState;
import java.time.LocalDateTime;

public record AdminOrderSummaryDTO(
	Long orderId,
	Long memberId,
	String nickname,
	OrderState state,
	LocalDateTime createdAt,
	int totalQuantity,
	int totalPrice
) {}
