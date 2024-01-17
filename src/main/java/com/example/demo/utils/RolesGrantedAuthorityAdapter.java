package com.example.demo.utils;

import com.example.demo.roles.Role;
import org.springframework.security.core.GrantedAuthority;

public class RolesGrantedAuthorityAdapter implements GrantedAuthority {
    private Role role;
    public RolesGrantedAuthorityAdapter(Role role){
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return this.role.getRole();
    }
}
