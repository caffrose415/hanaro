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
			throw new IllegalStateException("인증 정보가 없습니다.");
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
			.orElseGet(() -> Cart.builder().member(m).build()); // 비어있는 장바구니 뷰
		return CartResponseDTO.from(cart);
	}

	@Override
	public CartResponseDTO addItem(Authentication auth, CartAddRequestDTO req) {
		Member m = resolveMember(auth);
		Cart cart = getOrCreateCart(m);

		Item item = itemRepository.findByIdAndDeleteAtIsNull(req.id())
			.orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

		CartItem ci = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId())
			.orElseGet(() -> {
				CartItem n = CartItem.builder()
					.cart(cart)
					.item(item)
					.cnt(0)
					.price(item.getPrice()) // 단가 스냅샷
					.build();
				cart.getCartItems().add(n);
				return n;
			});

		ci.setCnt(ci.getCnt() + req.cnt());
		return CartResponseDTO.from(cart);
	}

	@Override
	public CartResponseDTO updateItem(Authentication auth, Long cartItemId, CartUpdateRequestDTO req) {
		Member m = resolveMember(auth);
		Cart cart = getOrCreateCart(m);

		CartItem ci = cartItemRepository.findById(cartItemId)
			.orElseThrow(() -> new IllegalArgumentException("장바구니 항목이 없습니다."));
		if (!ci.getCart().getId().equals(cart.getId()))
			throw new IllegalArgumentException("본인 장바구니가 아닙니다.");

		ci.setCnt(req.cnt());
		// 필요시 최신 단가 갱신: ci.setPrice(ci.getItem().getPrice());
		return CartResponseDTO.from(cart);
	}

	@Override
	public void removeItem(Authentication auth, Long cartItemId) {
		Member m = resolveMember(auth);
		Cart cart = getOrCreateCart(m);

		CartItem ci = cartItemRepository.findById(cartItemId)
			.orElseThrow(() -> new IllegalArgumentException("장바구니 항목이 없습니다."));
		if (!ci.getCart().getId().equals(cart.getId()))
			throw new IllegalArgumentException("본인 장바구니가 아닙니다.");

		cart.getCartItems().remove(ci);
		ci.setCart(null);
		cartItemRepository.delete(ci);
	}

}
