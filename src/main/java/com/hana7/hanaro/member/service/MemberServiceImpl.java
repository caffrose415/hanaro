package com.hana7.hanaro.member.service;

import com.hana7.hanaro.cart.entity.Cart;
import com.hana7.hanaro.cart.repository.CartRepository;
import com.hana7.hanaro.member.dto.LoginRequestDto;
import com.hana7.hanaro.member.dto.SignupRequestDto;
import com.hana7.hanaro.member.entity.Auth;
import com.hana7.hanaro.member.entity.Member;
import com.hana7.hanaro.member.repository.MemberRepository;
import com.hana7.hanaro.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void signup(SignupRequestDto signupRequestDto) {
        if (memberRepository.findByEmail(signupRequestDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        Cart newCart = Cart.builder().build();
        cartRepository.save(newCart);

        Member newMember = Member.builder()
                .email(signupRequestDto.getEmail())
                .password(passwordEncoder.encode(signupRequestDto.getPassword()))
                .nickname(signupRequestDto.getNickname())
                .auth(Auth.USERS)
                .cart(newCart)
                .build();

        memberRepository.save(newMember);
    }

    @Override
    public String login(LoginRequestDto loginRequestDto) {
        Optional<Member> result = memberRepository.findByEmail(loginRequestDto.getEmail());

        if (result.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        Member member = result.get();

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }

        Map<String, Object> claim = new HashMap<>();
        claim.put("email", member.getEmail());
        claim.put("nickname", member.getNickname());
        claim.put("auth", member.getAuth().name());

        return JwtUtil.generateToken(claim, 60); // Token valid for 60 minutes
    }
}