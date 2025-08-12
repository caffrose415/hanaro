package com.hana7.hanaro.cart.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hana7.hanaro.cart.dto.CartAddRequestDTO;
import com.hana7.hanaro.cart.dto.CartResponseDTO;
import com.hana7.hanaro.cart.dto.CartUpdateRequestDTO;
import com.hana7.hanaro.cart.entity.Cart;
import com.hana7.hanaro.cart.entity.CartItem;
import com.hana7.hanaro.cart.repository.CartItemRepository;
import com.hana7.hanaro.cart.repository.CartRepository;
import com.hana7.hanaro.common.exception.BusinessException;
import com.hana7.hanaro.common.exception.ErrorCode;
import com.hana7.hanaro.item.entity.Item;
import com.hana7.hanaro.item.repository.ItemRepository;
import com.hana7.hanaro.member.entity.Member;
import com.hana7.hanaro.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	private final ItemRepository itemRepository;
	private final MemberRepository memberRepository;

	private Member resolveMember(Authentication auth) {
		Object principal = auth.getPrincipal();
		if (!(principal instanceof com.hana7.hanaro.member.dto.UserDTO p)) {
			throw new BusinessException(ErrorCode.UNAUTHORIZED);
		}
		return memberRepository.findByEmail(p.getEmail());
	}

	private Cart getOrCreateCart(Member m) {
		return cartRepository.findByMemberId(m.getId())
			.orElseGet(() -> cartRepository.save(Cart.builder().member(m).build()));
	}

	@Override
	@Transactional(readOnly = true)
	public CartResponseDTO getMyCart(Authentication auth) {
		Member m = resolveMember(auth);
		Cart cart = cartRepository.findByMemberId(m.getId())
			.orElseGet(() -> Cart.builder().member(m).build());
		return CartResponseDTO.from(cart);
	}

	@Override
	public CartResponseDTO addItem(Authentication auth, CartAddRequestDTO req) {
		Member m = resolveMember(auth);
		Cart cart = getOrCreateCart(m);

		Item item = itemRepository.findByIdAndDeleteAtIsNull(req.id())
			.orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "상품을 찾을 수 없습니다."));

		if (item.getStock() <= 0) {
			throw new BusinessException(ErrorCode.INVALID_INPUT, "재고가 없습니다.");
		}

		CartItem ci = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId())
			.orElseGet(() -> {
				CartItem n = CartItem.builder()
					.cart(cart)
					.item(item)
					.cnt(0)
					.price(item.getPrice())
					.build();
				cart.getCartItems().add(n);
				return n;
			});

		int newQty = ci.getCnt() + req.cnt();
		if (newQty > item.getStock()) {
			throw new BusinessException(
				ErrorCode.INVALID_INPUT,
				"재고 부족: 요청 수량(" + newQty + ")이 재고(" + item.getStock() + ")를 초과합니다."
			);
		}

		ci.setCnt(newQty);
		return CartResponseDTO.from(cart);
	}

	@Override
	public CartResponseDTO updateItem(Authentication auth, Long cartItemId, CartUpdateRequestDTO req) {
		Member m = resolveMember(auth);
		Cart cart = getOrCreateCart(m);

		CartItem ci = cartItemRepository.findById(cartItemId)
			.orElseThrow(() -> new BusinessException(ErrorCode.INVALID_INPUT));
		if (!ci.getCart().getId().equals(cart.getId()))
			throw new BusinessException(ErrorCode.INVALID_INPUT);

		ci.setCnt(req.cnt());
		return CartResponseDTO.from(cart);
	}

	@Override
	public void removeItem(Authentication auth, Long cartItemId) {
		Member m = resolveMember(auth);
		Cart cart = getOrCreateCart(m);

		CartItem ci = cartItemRepository.findById(cartItemId)
			.orElseThrow(() -> new BusinessException(ErrorCode.INVALID_INPUT));
		if (!ci.getCart().getId().equals(cart.getId()))
			throw new BusinessException(ErrorCode.INVALID_INPUT);

		cart.getCartItems().remove(ci);
		ci.setCart(null);
		cartItemRepository.delete(ci);
	}

}
