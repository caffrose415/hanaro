package com.hana7.hanaro.order.controller;

import com.hana7.hanaro.order.dto.AdminOrderDetailDTO;
import com.hana7.hanaro.order.dto.AdminOrderSummaryDTO;
import com.hana7.hanaro.order.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/admin/orders")
@Tag(name = "관리자 주문조회")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService service;

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping
	@Operation(summary = "주문 목록 조회(전체/필터)")
	public ResponseEntity<List<AdminOrderSummaryDTO>> search(
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
		@RequestParam(required = false) Long itemId,
		@RequestParam(required = false) Long memberId,
		@RequestParam(required = false) String memberEmail
	) {
		return ResponseEntity.ok(service.search(startDate, endDate, itemId, memberId, memberEmail));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/{orderId}")
	@Operation(summary = "주문 상세 조회")
	public ResponseEntity<AdminOrderDetailDTO> detail(@PathVariable Long orderId) {
		return ResponseEntity.ok(service.detail(orderId));
	}
}
