package com.example.demo.security;

import com.example.demo.utils.JwtTokenUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JWTFilter extends OncePerRequestFilter {
    private final String HEADER_NAME = "Authorization";
    private final String BEARER_PREFIX = "Bearer";
    private final UserDetailsService uds;
    private final JwtTokenUtils jwtTokenUtils = new JwtTokenUtils();
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
//        var authHeader = request.getHeader(HEADER_NAME);
//        if(authHeader.isEmpty() || !authHeader.startsWith(BEARER_PREFIX)){
//            filterChain.doFilter(request, response);
//        }else{
//            String jwt = authHeader.substring(BEARER_PREFIX.length());
//            String username = jwtTokenUtils.getUserName(jwt);
//            if(!username.isEmpty() && SecurityContextHolder.getContext().getAuthentication()==null){
//                UserDetails ud = this.uds.loadUserByUsername(username);
//
//            }
//        }
        filterChain.doFilter(request, response);
    }
}
