package com.example.wippo.domain.auth.onboarding.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.example.wippo.domain.auth.onboarding.entity.OnboardingSession;
import com.example.wippo.domain.auth.onboarding.repository.OnboardingSessionRepository;
import com.example.wippo.domain.auth.social.Provider;
import com.example.wippo.domain.auth.social.dto.SocialLoginStartResponse;
import com.example.wippo.domain.auth.social.entity.UserSocialAccount;
import com.example.wippo.domain.auth.social.provider.SocialClient;
import com.example.wippo.domain.auth.social.repository.UserSocialAccountRepository;
import com.example.wippo.domain.auth.token.TokenPair;
import com.example.wippo.domain.auth.token.TokenService;
import com.example.wippo.domain.user.User;
import com.example.wippo.domain.user.UserRepository;
import com.example.wippo.domain.user.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OnboardingService {
    public static record FinalizeResult(Long userId, String displayName, TokenPair tokens) {}

    private final OnboardingSessionRepository onboardingRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final UserSocialAccountRepository usaRepository;
    private final TokenService tokenService;

    public SocialLoginStartResponse createFromSocial(SocialClient.SocialProfile p) {
        var id = java.util.UUID.randomUUID().toString();

        String suggested = normalizeNickname(p.nickname(), p.provider(), p.providerUserId());
        
        var s = OnboardingSession.builder()
            .id(id)
            .provider(p.provider())
            .providerUserId(p.providerUserId())
            .email(p.email())
            .suggestedDisplayName(suggested)
            .expiresAt(LocalDateTime.now().plusMinutes(10))
            .build();

        onboardingRepository.save(s);

        return new SocialLoginStartResponse(id, suggested);
    }

    /** 전화 인증 성공 이후 "최종 로그인" 처리 */
    @Transactional
    public FinalizeResult finalizeLogin(String onboardingId, String phoneE164) {
        var s = onboardingRepository.findById(onboardingId)
            .orElseThrow(() -> new IllegalStateException("invalid onboarding"));

        if (s.isExpired()) throw new IllegalStateException("onboarding expired");

        // 1) 유저 생성/획득 + 인증 마킹
        User user = userRepository.findByPhoneNumber(phoneE164)
            .orElseGet(() -> userRepository.save(User.builder()
                .phoneNumber(phoneE164)
                .status(User.AccountStatus.ACTIVE)
                .phoneVerifiedAt(LocalDateTime.now())
                .displayName(s.getSuggestedDisplayName())  // 최종 표시 이름 확정
                .build()));

        if (user.getPhoneVerifiedAt() == null) 
            userService.markPhoneVerified(user);

        // 2) 소셜 연결 upsert
        usaRepository.findByProviderAndProviderUserId(s.getProvider(), s.getProviderUserId())
            .orElseGet(() -> usaRepository.save(UserSocialAccount.builder()
                .user(user)
                .provider(s.getProvider())
                .providerUserId(s.getProviderUserId())
                .email(s.getEmail())
                .build()));

        // 3) 토큰 발급
        var tokens = tokenService.issue(user.getId());

        // 4) 세션 정리(선택: 즉시 삭제)
        onboardingRepository.delete(s);

        return new FinalizeResult(user.getId(), user.getDisplayName(), tokens);
    }

    private String normalizeNickname(String raw, Provider provider, String providerUserId) {
        String base = (raw == null || raw.isBlank())
            ? (provider.name().toLowerCase() + "_" + providerUserId)
            : raw.trim().replaceAll("\\s+", " ");
        return base.length() > 80 ? base.substring(0, 80) : base;
    }
}