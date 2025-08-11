package com.hana7.hanaro.order.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import com.hana7.hanaro.order.dto.AdminOrderDetailDTO;
import com.hana7.hanaro.order.dto.AdminOrderSummaryDTO;
import com.hana7.hanaro.order.dto.MyOrderSummaryDTO;
import com.hana7.hanaro.order.dto.OrderResponseDTO;

public interface OrderService {
	OrderResponseDTO checkout(Authentication auth);
	List<AdminOrderSummaryDTO> search(
		LocalDate startDate, LocalDate endDate,
		Long itemId, Long memberId, String memberEmail
	);
	AdminOrderDetailDTO detail(Long orderId);
	List<MyOrderSummaryDTO> list(Authentication auth, LocalDate startDate, LocalDate endDate);
	OrderResponseDTO detail(Authentication auth, Long orderId);
}
