package com.example.wippo.domain.auth.social;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.wippo.domain.auth.social.dto.SocialLoginStartRequest;
import com.example.wippo.domain.auth.social.dto.SocialLoginStartResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth/social")
@RequiredArgsConstructor
public class SocialAuthController {
    private final SocialAuthService socialAuthService;

    @PostMapping("/start")
    public SocialLoginStartResponse start(@RequestBody SocialLoginStartRequest req) {
        return socialAuthService.start(req);
    }
}
