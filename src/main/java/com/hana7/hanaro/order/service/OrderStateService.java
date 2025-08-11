package com.hana7.hanaro.order.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hana7.hanaro.order.entity.OrderState;
import com.hana7.hanaro.order.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderStateService {
	private final OrderRepository orderRepository;

	@Transactional
	public void advanceStates() {
		LocalDateTime now = LocalDateTime.now();

		orderRepository.updateStateBatch(OrderState.PAYED,      now.minusMinutes(OrderState.PAYED.stateInterval()),      OrderState.PREPARING);
		orderRepository.updateStateBatch(OrderState.PREPARING,  now.minusMinutes(OrderState.PREPARING.stateInterval()),  OrderState.TRANSITING);
		orderRepository.updateStateBatch(OrderState.TRANSITING, now.minusMinutes(OrderState.TRANSITING.stateInterval()), OrderState.DELIVERED);
	}
}
