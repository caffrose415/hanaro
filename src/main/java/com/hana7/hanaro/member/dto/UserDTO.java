package com.hana7.hanaro.member.dto;

import com.hana7.hanaro.member.entity.Auth;
import lombok.Getter;
import lombok.Setter;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class UserDTO extends User {
    private String email;
    private String nickname;
    private Auth auth;
    private String password;

    public UserDTO(String username, String password, String nickname, Auth auth) {
        super(username, password, List.of(new SimpleGrantedAuthority("ROLE_"+auth.name())));
        this.email = username;
        this.nickname = nickname;
        this.auth = auth;
        this.password = password;
    }

    public Map<String, Object> getClaims() {
        Map<String, Object> map = new HashMap<>();
        map.put("email", email);
        map.put("nickname", nickname);
        map.put("auth",auth);

        return map;
    }
}
