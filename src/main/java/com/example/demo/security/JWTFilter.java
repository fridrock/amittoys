package com.example.demo.security;

import com.example.demo.utils.JwtTokenStatus;
import com.example.demo.utils.JwtTokenUtils;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;


@Component
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {
    private final String HEADER_NAME = "Authorization";
    private final int BEARER_PREFIX = "Bearer".length()+1;
    private final JwtTokenUtils jwtTokenUtils;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = extractJwtTokenFromRequest(request);
        if(StringUtils.isEmpty(jwtToken)){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            filterChain.doFilter(request, response);
        }else if(jwtTokenUtils.isTokenValid(jwtToken) == JwtTokenStatus.WRONG){
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Token is wrong");
        }else if(jwtTokenUtils.isTokenValid(jwtToken) == JwtTokenStatus.EXPIRED){
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Token is expired");
        } else{
            var username = jwtTokenUtils.getUserName(jwtToken);
            var roles = jwtTokenUtils.getRoles(jwtToken);
            //TODO make custom authentication
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            roles
                            );
            SecurityContextHolder.getContext().setAuthentication(authToken);
            filterChain.doFilter(request, response);
        }
    }
    private String extractJwtTokenFromRequest(HttpServletRequest request){
        var authHeader = request.getHeader(HEADER_NAME);
        if(StringUtils.isEmpty(authHeader)){
            return "";
        }else{
            return authHeader.substring(BEARER_PREFIX);
        }
    }
}


