package com.hana7.hanaro.order.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.hana7.hanaro.order.dto.AdminOrderSummaryDTO;
import com.hana7.hanaro.order.entity.Order;

public interface OrderQueryRepository {
	List<AdminOrderSummaryDTO> searchSummaries(
		LocalDateTime start, LocalDateTime end,
		Long itemId, Long memberId);

	Optional<Order> findDetailById(Long id);
}
