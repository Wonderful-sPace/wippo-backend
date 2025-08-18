package com.example.wippo.domain.auth.social.dto;

import com.example.wippo.domain.auth.social.Provider;

public record SocialLoginStartRequest(
    Provider provider,  // "GOOGLE" | "KAKAO"
    String accessToken, // KAKAO는 주로 accessToken 사용
    String idToken      // 클라이언트 측 id_token
) {
}
