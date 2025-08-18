package com.example.wippo.domain.auth.onboarding.dto;

public record LoginResponse(
    Long userId, 
    String displayName, 
    String accessToken,
    String refreshToken
) {

}
