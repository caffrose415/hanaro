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
import java.util.HashMap;
import java.util.Map;

public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        Map<String, Object> claims = JwtUtil.authenticationToClaim(authentication);

        ObjectMapper objectMapper = new ObjectMapper();
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println(objectMapper.writeValueAsString(claims));
        out.close();
    }
}
