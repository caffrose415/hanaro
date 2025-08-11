package com.hana7.hanaro.order.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.hana7.hanaro.cart.entity.Cart;
import com.hana7.hanaro.cart.entity.CartItem;
import com.hana7.hanaro.cart.repository.CartItemRepository;
import com.hana7.hanaro.cart.repository.CartRepository;
import com.hana7.hanaro.item.entity.Item;
import com.hana7.hanaro.item.repository.ItemRepository;
import com.hana7.hanaro.member.entity.Member;
import com.hana7.hanaro.member.repository.MemberRepository;
import com.hana7.hanaro.order.dto.AdminOrderDetailDTO;
import com.hana7.hanaro.order.dto.AdminOrderSummaryDTO;
import com.hana7.hanaro.order.dto.MyOrderSummaryDTO;
import com.hana7.hanaro.order.dto.OrderResponseDTO;
import com.hana7.hanaro.order.entity.Order;
import com.hana7.hanaro.order.entity.OrderItem;
import com.hana7.hanaro.order.entity.OrderState;
import com.hana7.hanaro.order.repository.OrderItemRepository;
import com.hana7.hanaro.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

	private final MemberRepository memberRepository;
	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	private final ItemRepository itemRepository;
	private final OrderRepository orderRepository;
	private final OrderItemRepository orderItemRepository;

	private Member resolveMember(Authentication auth) {
		Object principal = auth.getPrincipal();
		if (!(principal instanceof com.hana7.hanaro.member.dto.UserDTO p)) {
			throw new IllegalStateException("인증 정보가 없습니다.");
		}
		return memberRepository.findByEmail(p.getEmail());
	}

	@Override
	public OrderResponseDTO checkout(Authentication auth) {
		Member m = resolveMember(auth);
		Cart cart = cartRepository.findByMemberId(m.getId())
			.orElseThrow(() -> new IllegalArgumentException("장바구니가 비어있습니다."));

		if (cart.getCartItems().isEmpty()) {
			throw new IllegalArgumentException("장바구니가 비어있습니다.");
		}

		// 주문 생성 (초기 상태: PAYED)
		Order order = Order.builder()
			.member(m)
			.state(OrderState.PAYED)
			.build();

		// 각 카트 아이템 → 주문 아이템 생성 + (선택) 재고 차감
		for (CartItem ci : cart.getCartItems()) {
			Item item = itemRepository.findByIdAndDeleteAtIsNull(ci.getItem().getId())
				.orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다. id=" + ci.getItem().getId()));

			// 재고 체크/차감 (원치 않으면 이 블록 제거)
			if (item.getStock() < ci.getCnt()) {
				throw new IllegalArgumentException("재고 부족: " + item.getName());
			}
			item.setStock(item.getStock() - ci.getCnt());

			OrderItem oi = OrderItem.builder()
				.orders(order)          // mappedBy="orders"
				.item(item)
				.price(ci.getPrice())   // 장바구니 담을 당시 단가 스냅샷
				.cnt(ci.getCnt())
				.build();

			order.getOrderItems().add(oi);
		}

		// 저장 (cascade = ALL이므로 order만 save해도 orderItems 저장됨)
		order = orderRepository.save(order);
		order.setStatedAt(LocalDateTime.now());


		// 장바구니 비우기 (내 카트만)
		// cartItemRepository.deleteByCartId(cart.getId());
		// cart.getCartItems().clear();
		cartRepository.deleteById(cart.getId());
		// cart.setDeleteAt(java.time.LocalDateTime.now());

		return OrderResponseDTO.from(order);
	}

	@Override
	public List<AdminOrderSummaryDTO> search(
		LocalDate startDate, LocalDate endDate,
		Long itemId, Long memberId, String memberEmail
	) {
		LocalDateTime start = (startDate == null) ? null : startDate.atStartOfDay();
		LocalDateTime end   = (endDate   == null) ? null : endDate.plusDays(1).atStartOfDay();

		Long effectiveMemberId = memberId;
		if (effectiveMemberId == null && memberEmail != null && !memberEmail.isBlank()) {
			Member m = memberRepository.findByEmail(memberEmail);
			effectiveMemberId = (m != null) ? m.getId() : null;
		}

		return orderRepository.searchSummaries(start, end, itemId, effectiveMemberId);
	}

	@Override
	public AdminOrderDetailDTO detail(Long orderId) {
		var order = orderRepository.findDetailById(orderId)
			.orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));
		return AdminOrderDetailDTO.from(order);
	}

	@Override
	public List<MyOrderSummaryDTO> list(Authentication auth, LocalDate startDate, LocalDate endDate) {
		Member me = resolveMember(auth);

		List<Order> orders;
		if (startDate == null && endDate == null) {
			orders = orderRepository.findByMemberIdOrderByCreatedAtDesc(me.getId());
		} else {
			LocalDateTime start = (startDate == null) ? LocalDate.MIN.atStartOfDay() : startDate.atStartOfDay();
			LocalDateTime end   = (endDate == null)   ? LocalDate.MAX.atStartOfDay() : endDate.plusDays(1).atStartOfDay();
			orders = orderRepository.findByMemberIdAndCreatedAtBetweenOrderByCreatedAtDesc(me.getId(), start, end);
		}

		return orders.stream().map(MyOrderSummaryDTO::from).toList();
	}

	@Override
	public OrderResponseDTO detail(Authentication auth, Long orderId) {
		Member me = resolveMember(auth);
		Order order = orderRepository.findByIdAndMemberId(orderId, me.getId())
			.orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없거나 권한이 없습니다."));
		return OrderResponseDTO.from(order);
	}

}
