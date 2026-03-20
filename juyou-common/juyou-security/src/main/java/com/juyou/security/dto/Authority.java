package com.juyou.security.dto;

import org.springframework.security.core.GrantedAuthority;

public class Authority implements GrantedAuthority {
    String role;

    @Override
    public String getAuthority() {
        return role;
    }
    public Authority(String role){
        this.role=role;
    }
}
