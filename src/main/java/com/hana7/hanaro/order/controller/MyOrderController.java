package com.hana7.hanaro.order.controller;

import com.hana7.hanaro.order.dto.MyOrderSummaryDTO;
import com.hana7.hanaro.order.dto.OrderResponseDTO;
import com.hana7.hanaro.order.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/user/orders")
@Tag(name = "[사용자] 주문조회")
@RequiredArgsConstructor
public class MyOrderController {

	private final OrderService orderService;

	@PreAuthorize("hasRole('USERS')")
	@GetMapping
	@Operation(summary = "내 주문 목록", description = "옵션: startDate/endDate=YYYY-MM-DD")
	public ResponseEntity<List<MyOrderSummaryDTO>> list(
		Authentication auth,
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
	) {
		return ResponseEntity.ok(orderService.list(auth, startDate, endDate));
	}

	@PreAuthorize("hasRole('USERS')")
	@GetMapping("/{orderId}")
	@Operation(summary = "내 주문 상세")
	public ResponseEntity<OrderResponseDTO> detail(Authentication auth, @PathVariable Long orderId) {
		return ResponseEntity.ok(orderService.detail(auth, orderId));
	}
}
