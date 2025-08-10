package com.hana7.hanaro.member.service;

import com.hana7.hanaro.member.dto.LoginRequestDto;
import com.hana7.hanaro.member.dto.SignupRequestDto;

public interface MemberService {
    void signup(SignupRequestDto signupRequestDto);
    String login(LoginRequestDto loginRequestDto);
}
