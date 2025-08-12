package com.hana7.hanaro.order.service;


import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderStateScheduler {
	private final OrderStateService service;

	@Scheduled(cron = "0 * * * * *")
	public void run() {
		service.advanceStates();
	}
}
