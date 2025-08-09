package com.hana7.hanaro.cart.repository;

import com.hana7.hanaro.RepositoryTest;
import com.hana7.hanaro.cart.entity.Cart;
import com.hana7.hanaro.cart.entity.CartItem;
import com.hana7.hanaro.item.entity.Item;
import com.hana7.hanaro.item.repository.ItemRepository;
import com.hana7.hanaro.member.entity.Auth;
import com.hana7.hanaro.member.entity.Member;
import com.hana7.hanaro.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class CartRepositoryTest extends RepositoryTest {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ItemRepository itemRepository;

    private Member testMember;
    private Item testItem;

    @BeforeEach
    void setup() {
        testMember = Member.builder()
                .email("test@gmail.com")
                .password("test123")
                .nickname("tester")
                .auth(Auth.USERS)
                .build();
        memberRepository.save(testMember);

        testItem = Item.builder()
                .name("테스트상품")
                .price(15000)
                .stock(50)
                .build();
        itemRepository.save(testItem);
    }

    @Test
    @Order(1)
    void addCartItemTest() {
        Cart cart = Cart.builder().member(testMember).build();
        cartRepository.save(cart);

        CartItem cartItem = CartItem.builder()
                .cart(cart)
                .item(testItem)
                .cnt(2)
                .build();

        cartItemRepository.save(cartItem);

        Cart foundCart = cartRepository.findById(cart.getId()).orElseThrow();
        CartItem foundCartItem = cartItemRepository.findById(cartItem.getId()).orElseThrow();

        assertThat(foundCart.getMember().getId()).isEqualTo(testMember.getId());
        assertThat(foundCartItem.getItem().getId()).isEqualTo(testItem.getId());
        assertThat(foundCartItem.getCnt()).isEqualTo(2);
    }

    @Test
    @Order(2)
    void findCartItemTest() {
        Cart cart = Cart.builder().member(testMember).build();
        cartRepository.save(cart);

        CartItem cartItem = CartItem.builder()
            .cart(cart)
            .item(testItem)
            .cnt(3)
            .build();
        cartItemRepository.save(cartItem);

        Cart foundCart = cartRepository.findById(cart.getId()).orElseThrow();
        CartItem foundCartItem = cartItemRepository.findById(cartItem.getId()).orElseThrow();

        assertThat(foundCart).isNotNull();
        assertThat(foundCart.getMember().getEmail()).isEqualTo("test@gmail.com");

        assertThat(foundCartItem).isNotNull();
        assertThat(foundCartItem.getCnt()).isEqualTo(3);
        assertThat(foundCartItem.getItem().getName()).isEqualTo("테스트상품");
    }

    @Test
    @Order(3)
    void updateCartItemCntTest() {
        Cart cart = Cart.builder().member(testMember).build();
        cartRepository.save(cart);
        CartItem cartItem = CartItem.builder().cart(cart).item(testItem).cnt(1).build();
        cartItemRepository.save(cartItem);

        CartItem savedCartItem = cartItemRepository.findById(cartItem.getId()).orElseThrow();
        CartItem updatedCartItem = CartItem.builder()
            .id(savedCartItem.getId())
            .cart(savedCartItem.getCart())
            .item(savedCartItem.getItem())
            .cnt(5)
            .build();
        cartItemRepository.save(updatedCartItem);

        CartItem foundCartItem = cartItemRepository.findById(cartItem.getId()).orElseThrow();
        assertThat(foundCartItem.getCnt()).isEqualTo(5);
    }

    @Test
    @Order(4)
    void deleteCartItemTest() {
        Cart cart = Cart.builder().member(testMember).build();
        cartRepository.save(cart);
        CartItem cartItem = CartItem.builder().cart(cart).item(testItem).cnt(1).build();
        cartItemRepository.save(cartItem);

        cartItemRepository.deleteById(cartItem.getId());

        assertThat(cartItemRepository.findById(cartItem.getId())).isEmpty();
    }
}
