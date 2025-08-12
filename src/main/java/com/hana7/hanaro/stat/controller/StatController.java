package com.hana7.hanaro.stat.controller;

import org.springframework.batch.core.BatchStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hana7.hanaro.order.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/stat")
@Tag(name="배치 작업")
@RequiredArgsConstructor
public class StatController {
	private final OrderService orderService;

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/statbatch")
	@Operation(summary = "매일 23시 59분 59초 작업 실시")
	public ResponseEntity<?> runStatBatch() throws Exception {
		BatchStatus batchStatus = orderService.runStatBatch();
		return ResponseEntity.ok("Batch Result: " + batchStatus);
	}
}
