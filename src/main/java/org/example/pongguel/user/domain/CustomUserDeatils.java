package org.example.pongguel.user.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDeatils implements UserDetails {
    private final User user;

    public CustomUserDeatils(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    // 비밀번호 사용하지 않음 (소셜 로그인 사용)
    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return user.getAccountEmail(); // accountEmail을 username으로 사용하기
    }

    // 계정 만료 확인
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
