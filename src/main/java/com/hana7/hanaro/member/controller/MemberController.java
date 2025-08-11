package com.hana7.hanaro.member.controller;

import com.hana7.hanaro.member.dto.LoginRequestDTO;
import com.hana7.hanaro.member.dto.SignupRequestDTO;
import com.hana7.hanaro.member.service.MemberService;
import com.hana7.hanaro.security.JwtUtil;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final AuthenticationManager authenicationManager;

    @PostMapping("/signin")
    @Tag(name="로그인")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        try {
            Authentication authenticate = authenicationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequestDTO.email(), loginRequestDTO.password()
                )
            );
            return ResponseEntity.ok(JwtUtil.authenticationToClaim(authenticate));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid!");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody SignupRequestDTO signupRequestDto) {
        memberService.signup(signupRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}


