package com.example.ex32_jpa_qnaboard_swagger.security;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.example.ex32_jpa_qnaboard_swagger.user.UserEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class TokenProvider {
    private static final String SECRET_KEY = "fsrsfWaFAfqYhD2dIA0TTB2ON1RzgYK3ql1ysV7iI2wGP9UKjbg7CifNLRrLY0tynyzUOPqM5o6ot5Mrc+utMbd+VG1LCJUiTrBiVMPlbLI=";

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String create(UserEntity userEntity) {

        Date expiryDate = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));

        return Jwts.builder()
                .setSubject(String.valueOf(userEntity.getId()))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String validateAndGetUserId(String token) {
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
        
        return claims.getSubject();
    }
}

