package com.example.wippo.domain.auth.phone.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.wippo.domain.auth.onboarding.dto.LoginResponse;
import com.example.wippo.domain.auth.onboarding.service.OnboardingService;
import com.example.wippo.domain.auth.phone.dto.SendCodeRequest;
import com.example.wippo.domain.auth.phone.dto.VerifyCodeRequest;
import com.example.wippo.domain.auth.phone.service.PhoneAuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth/phone")
@RequiredArgsConstructor
public class PhoneAuthController {
    private final PhoneAuthService phoneService;
    private final OnboardingService onboardingService;

    @PostMapping("/send")
    public ResponseEntity<Void> send(@RequestBody SendCodeRequest req){
        phoneService.sendCode(req.phoneNumber());
        return ResponseEntity.ok().build();
    }

    // 전화 인증 + 최종 로그인까지 한번에
    @PostMapping("/verify")
    public ResponseEntity<LoginResponse> verify(@RequestBody VerifyCodeRequest req){
        if (!phoneService.verifyCode(req.phoneNumber(), req.code())) {
            throw new IllegalArgumentException("invalid code");
        }

        var result = onboardingService.finalizeLogin(req.onboardingId(), req.phoneNumber());
        return ResponseEntity.ok(
            new LoginResponse(
                result.userId(), 
                result.displayName(), 
                result.tokens().accessToken(), 
                result.tokens().refreshToken()
            )
        );
    }

}