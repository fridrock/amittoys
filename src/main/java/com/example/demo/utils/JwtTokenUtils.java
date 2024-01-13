package com.example.demo.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.PublicKey;
import java.time.Duration;
import java.util.*;

@Component
@PropertySource("application.yml")
public class JwtTokenUtils {
    public static void main(String[] args) {
        var test = new JwtTokenUtils();
        System.out.println(test.generateToken());
    }
    @Value("${jwt.secret}")
    private String SECRET_KEY;
    //TODO make it with value
    private Duration jwtLifetime = Duration.ofMinutes(30);
    private SecretKey secretKey;
    private void createSecretKey(){
        var signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] secretKeyBytes = Base64.getDecoder().decode(SECRET_KEY);
        this.secretKey = new SecretKeySpec(secretKeyBytes, signatureAlgorithm.getJcaName());
    }
    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", ((UserDetailsAdapter)userDetails).getId());
        //TODO add roles
        Date issuedDate = new Date();
        Date expiredTime = new Date(issuedDate.getTime()+jwtLifetime.toMillis());
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(issuedDate)
                .expiration(expiredTime)
                .signWith(secretKey)
                .compact();
    }
    private Claims getAllClaimsFromToken(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }
    private String getClaim(String token, String claim){
        return getAllClaimsFromToken(token).get(claim).toString();
    }
    public String getUserName(String token){
        return getAllClaimsFromToken(token).getSubject();
    }
}

