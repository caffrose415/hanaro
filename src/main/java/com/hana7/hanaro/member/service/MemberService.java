package com.hana7.hanaro.member.service;

import java.util.List;

import com.hana7.hanaro.member.dto.LoginRequestDTO;
import com.hana7.hanaro.member.dto.MemberAdminDTO;
import com.hana7.hanaro.member.dto.SignupRequestDTO;

public interface MemberService {

    void signup(SignupRequestDTO signupRequestDTO);
    List<MemberAdminDTO> list(boolean includeDeleted);
    void softDelete(Long memberId);
}
