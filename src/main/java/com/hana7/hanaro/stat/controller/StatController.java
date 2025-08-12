package com.hana7.hanaro.stat.controller;

import org.springframework.batch.core.BatchStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hana7.hanaro.order.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/stat")
@RequiredArgsConstructor
public class StatController {
	private final OrderService orderService;

	@GetMapping("/statbatch")
	public ResponseEntity<?> runStatBatch() throws Exception {
		BatchStatus batchStatus = orderService.runStatBatch();
		return ResponseEntity.ok("Batch Result: " + batchStatus);
	}
}
