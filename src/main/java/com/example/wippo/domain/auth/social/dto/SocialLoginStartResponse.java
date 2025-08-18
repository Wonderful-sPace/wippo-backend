package com.example.wippo.domain.auth.social.dto;

public record SocialLoginStartResponse(
    String onboardingId,
    String suggestedName
) {
}