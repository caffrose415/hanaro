package com.hana7.hanaro.order.service;

import com.hana7.hanaro.order.entity.OrderState;
import com.hana7.hanaro.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderStateScheduler {
	private final OrderStateService service;

	@Scheduled(cron = "0 * * * * *")
	public void run() {
		service.advanceStates();
	}
}
