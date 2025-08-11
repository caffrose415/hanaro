package com.hana7.hanaro.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


public record SignupRequestDTO (
    @NotBlank(message = "이메일을 입력해 주세요")
    @Email(message = "이메일 형식을 지켜주세요")
    @Size(max = 25, message = "이메일은 최대 25자리까지 가능합니다")
    @Schema(name="email", example = "test@gmail.com")
    String email,

    @NotBlank(message = "비밀번호를 입력해주세요")
    @Size(max = 15, message = "비밀번호는 최대 15자까지 가능합니다.")
    @Schema(name = "password", example = "test123")
    String password,

    @NotBlank(message = "닉네임을 입력해주세요")
    @Size(max = 15, message = "닉네임은 최대 15자까지 가능합니다.")
    @Schema(name = "nickname", example = "tester")
    String nickname
){}
