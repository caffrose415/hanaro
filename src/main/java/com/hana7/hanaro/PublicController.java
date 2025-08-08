package com.hana7.hanaro;

import org.springframework.batch.core.BatchStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class PublicController {
	private final MemoService memoService;

	@GetMapping("/batch")
	public ResponseEntity<?> runBatch() throws Exception {
		BatchStatus batchStatus = memoService.runBatch();
		return ResponseEntity.ok("Batch Result: " + batchStatus);
	}
}
