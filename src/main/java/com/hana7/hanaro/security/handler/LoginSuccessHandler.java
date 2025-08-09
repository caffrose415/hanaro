package com.hana7.hanaro.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hana7.hanaro.member.dto.UserDto;
import com.hana7.hanaro.security.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        System.out.println("** SuccessHandler = " + authentication);

        UserDto dto = (UserDto) authentication.getPrincipal();
        Map<String, Object> claims = Map.of(
                "email", dto.getEmail(),
                "nickname", dto.getNickname(),
                "auth", dto.getAuth().name()
        );
        String accessToken = JwtUtil.generateToken(claims, 10);
        String refreshToken = JwtUtil.generateToken(claims, 60 * 24);

        Map<String, String> tokens = Map.of("accessToken", accessToken, "refreshToken", refreshToken);

        ObjectMapper objectMapper = new ObjectMapper();
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println(objectMapper.writeValueAsString(tokens));
        out.close();
    }
}
