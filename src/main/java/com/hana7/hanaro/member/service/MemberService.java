package com.hana7.hanaro.member.service;

import com.hana7.hanaro.member.dto.LoginRequestDTO;
import com.hana7.hanaro.member.dto.SignupRequestDTO;

public interface MemberService {

    void signup(SignupRequestDTO signupRequestDTO);
}
