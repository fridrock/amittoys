package com.example.demo.utils;


import io.jsonwebtoken.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Duration;
import java.util.*;

@Component
public class JwtTokenUtils {
    private Duration jwtLifetime = Duration.ofMinutes(1);
    private SecretKey secretKey;
    private JwtParser jwtParser;
    private String SECRET_KEY = "secretasdflksajdflkasjdflkjassadfasdjfaklsdjlfakdkjfalksdjfsjlklkfjaslfjsajjldsals";
    public JwtTokenUtils(){
        var signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] secretKeyBytes = Base64.getDecoder().decode(SECRET_KEY);
        this.secretKey = new SecretKeySpec(secretKeyBytes, signatureAlgorithm.getJcaName());
        this.jwtParser = Jwts.parser().verifyWith(this.secretKey).build();
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
        return jwtParser.parseSignedClaims(token).getPayload();
    }
    private String getClaim(String token, String claim){
        return getAllClaimsFromToken(token).get(claim).toString();
    }
    public String getUserName(String token){
        return getAllClaimsFromToken(token).getSubject();
    }
    public JwtTokenStatus isTokenValid(String token){
        try{
            getAllClaimsFromToken(token);
            return JwtTokenStatus.OK;
        }catch (ExpiredJwtException e){
            return JwtTokenStatus.EXPIRED;
        }catch (JwtException e){
            return JwtTokenStatus.WRONG;
        }
    }
}

