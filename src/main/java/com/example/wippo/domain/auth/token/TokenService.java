package com.example.wippo.domain.auth.token;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final JwtProvider jwt;
    public TokenPair issue(Long userId) {
        return new TokenPair(jwt.createAccess(userId), jwt.createRefresh(userId));
    }   
}
