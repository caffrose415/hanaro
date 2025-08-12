package com.hana7.hanaro.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hana7.hanaro.member.dto.UserDTO;
import com.hana7.hanaro.member.entity.Auth;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import java.util.Map;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private final String[] excludePatterns = {
            "/api/member/signin",
            "/api/member/signup",
            "/user/items/**",
            "/actuator/**",
            "/swagger-ui/**",
            "/swagger.html",
            "/v3/api-docs/**",
            "/hanaweb/api-docs/**"
    };

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return Arrays.stream(excludePatterns)
                .anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        try {
            System.out.println("** JwtAuthenticationFilter.doFilterInternal:" + authHeader.substring(7));
            Map<String, Object> claims = JwtUtil.validateToken(authHeader.substring(7));

            String email = (String)claims.get("email");
            String nickname = (String)claims.get("nickname");
            String roleName = (String) claims.get("auth");


            Auth auth = Auth.valueOf(roleName);

            UserDTO userDTO = new UserDTO(
                email, "",
                nickname, auth
            );

            UsernamePasswordAuthenticationToken authenticationToken = new
                UsernamePasswordAuthenticationToken(userDTO, null, userDTO.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } catch (Exception e) {
            e.printStackTrace(System.out);
            response.setContentType("application/json");
            ObjectMapper objectMapper = new ObjectMapper();
            PrintWriter out = response.getWriter();
            out.println(objectMapper.writeValueAsString(Map.of("error", "ERROR_ACCESS_TOKEN")));
            out.close();
        }
        filterChain.doFilter(request, response);
    }
}

