package com.example.wippo.domain.auth.token;

import java.time.Instant;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

// MVP: access 30m, refresh 14d
@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secret;

    public String createAccess(Long userId) {
        var now = Instant.now();
        return Jwts.builder()
            .subject(String.valueOf(userId))
            .issuedAt(Date.from(now))
            .expiration(Date.from(now.plusSeconds(1800)))
            .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
            .compact();
    }

    public String createRefresh(Long userId) {
        var now = Instant.now();
        return Jwts.builder()
            .subject(String.valueOf(userId))
            .issuedAt(Date.from(now))
            .expiration(Date.from(now.plusSeconds(14 * 24 * 3600)))
            .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
            .compact();
    }
}
