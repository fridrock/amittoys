package com.example.demo.utils;

import com.example.demo.roles.Role;
import org.springframework.security.core.GrantedAuthority;

public class RolesGrantedAuthorityAdapter implements GrantedAuthority {
    private String authority;
    public RolesGrantedAuthorityAdapter(String authority){
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return this.authority;
    }
}
