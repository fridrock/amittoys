package com.example.demo.utils;


import io.jsonwebtoken.*;
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
    @Value("${jwt.secret}")
    private String SECRET_KEY;
    //TODO make it with value
    private Duration jwtLifetime = Duration.ofMinutes(30);
    private SecretKey secretKey;
    private JwtParser jwtParser;
    private void createJwtParser(){
        if(this.jwtParser==null){
            this.jwtParser = Jwts.parser().verifyWith(this.secretKey).build();
        }
    }
    private void createSecretKey(){
        if(this.secretKey==null){
            var signatureAlgorithm = SignatureAlgorithm.HS256;
            byte[] secretKeyBytes = Base64.getDecoder().decode(SECRET_KEY);
            this.secretKey = new SecretKeySpec(secretKeyBytes, signatureAlgorithm.getJcaName());
        }
    }
    public String generateToken(UserDetails userDetails){
        createSecretKey();
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
        createJwtParser();
        return jwtParser.parseSignedClaims(token).getPayload();
    }
    private String getClaim(String token, String claim){
        return getAllClaimsFromToken(token).get(claim).toString();
    }
    public String getUserName(String token){
        return getAllClaimsFromToken(token).getSubject();
    }
    public boolean isTokenValid(String token){
        createSecretKey();
        try{
            var exp = getAllClaimsFromToken(token).getExpiration();
            var now = new Date();
            return now.before(exp);
        }catch (JwtException e){
            return false;
        }
    }
}

