package com.hana7.hanaro.member.dto;

import com.hana7.hanaro.member.entity.Auth;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
@Setter
public class UserDto extends User {
    private String email;
    private String nickname;
    private Auth auth;

    public UserDto(String username, String password, Collection<? extends GrantedAuthority> authorities, String nickname, Auth auth) {
        super(username, password, authorities);
        this.email = username;
        this.nickname = nickname;
        this.auth = auth;
    }
}
