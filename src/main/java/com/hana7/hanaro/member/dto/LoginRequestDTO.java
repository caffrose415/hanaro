package com.hana7.hanaro.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;


public record LoginRequestDTO (
    @NotBlank(message = "이메일을 입력해주세요")
    @Schema(name = "email", example = "test@gmail.com")
    String email,

    @NotBlank(message = "비밀번호를 입력해주세요")
    @Schema(name="password", example = "12345678")
    @Min(4)
    String password
){}
