package com.hana7.hanaro.member.service;

import com.hana7.hanaro.member.dto.LoginRequestDTO;
import com.hana7.hanaro.member.dto.SignupRequestDTO;
import com.hana7.hanaro.member.entity.Auth;
import com.hana7.hanaro.member.entity.Member;
import com.hana7.hanaro.member.repository.MemberRepository;
import com.hana7.hanaro.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String login(LoginRequestDTO loginRequestDTO) {
        Member member = memberRepository.findByEmail(loginRequestDTO.email())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));

        if (!passwordEncoder.matches(loginRequestDTO.password(), member.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        Map<String, Object> claims = Map.of(
                "email", member.getEmail(),
                "nickname", member.getNickname(),
                "roleNames", member.getAuth().name()
        );

        return JwtUtil.generateToken(claims, 30);
    }

    @Override
    public void signup(SignupRequestDTO signupRequestDTO) {
        if (memberRepository.existsByEmail(signupRequestDTO.email())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        Member member = Member.builder()
                .email(signupRequestDTO.email())
                .password(passwordEncoder.encode(signupRequestDTO.password()))
                .nickname(signupRequestDTO.nickname())
                .auth(Auth.USERS)
                .build();

        memberRepository.save(member);
    }
}
