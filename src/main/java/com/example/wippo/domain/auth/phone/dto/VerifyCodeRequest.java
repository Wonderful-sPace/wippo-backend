package com.example.wippo.domain.auth.phone.dto;

public record VerifyCodeRequest(String onboardingId, String phoneNumber, String code) {
    
}
