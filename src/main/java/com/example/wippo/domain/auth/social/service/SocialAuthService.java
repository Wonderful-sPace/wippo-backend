package com.example.wippo.domain.auth.social.service;

import org.springframework.stereotype.Service;

import com.example.wippo.domain.auth.onboarding.service.OnboardingService;
import com.example.wippo.domain.auth.social.dto.SocialLoginStartRequest;
import com.example.wippo.domain.auth.social.dto.SocialLoginStartResponse;
import com.example.wippo.domain.auth.social.provider.SocialClient;
import com.example.wippo.domain.auth.social.provider.SocialClientRegistry;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SocialAuthService {
    private final SocialClientRegistry registry;
    private final OnboardingService onboarding;

    public SocialLoginStartResponse start(SocialLoginStartRequest req) {
        // 1) 요청된 provider에 맞는 구현체 선택
        SocialClient client = registry.require(req.provider());

        // // 요청 provider로 강제 고정 (test용)
        // var fixed = new SocialClient.SocialProfile(
        //     req.provider(), profile.providerUserId(), profile.name(), profile.email()
        // );

        // 2) 공급자별 토큰 검증 + 프로필 조회
        SocialClient.SocialProfile profile = client.verify(req);

        // 3) 온보딩/가입·로그인 처리 → Wippo 서비스 토큰 발급
        return onboarding.createFromSocial(profile);
    }
}
