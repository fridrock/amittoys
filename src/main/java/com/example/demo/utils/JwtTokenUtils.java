package com.example.demo.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtTokenUtils {
    //TODO replace with @Value
    private final String secret = "secretasdflksajdflkasjdflkjassadfasdjfaklsdjlfakdkjfalksdjfsjlklkfjaslfjsajjldsals";
    private Duration jwtLifetime = Duration.ofMinutes(30);
    private SecretKey secretKey;
    public JwtTokenUtils(){
        // decode the base64 encoded string
        //byte[] decodedKey = Base64.getDecoder().decode(secret);
        // rebuild key using SecretKeySpec
        //this.secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }
    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        //for future roles
        List<String> rolesList = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        claims.put("roles", rolesList);
        Date issuedDate = new Date();
        Date expiredTime = new Date(issuedDate.getTime()+jwtLifetime.toMillis());
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(issuedDate)
                .expiration(expiredTime)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }
    public String getUserName(String token){
        return getAllClaimsFromToken(token).getSubject();
    }
    public List<String> getRoles(String token){
        return getAllClaimsFromToken(token).get("roles", List.class);
    }
    private Claims getAllClaimsFromToken(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }
}

