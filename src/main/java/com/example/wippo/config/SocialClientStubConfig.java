package com.example.wippo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.example.wippo.domain.auth.social.Provider;
import com.example.wippo.domain.auth.social.dto.SocialLoginStartRequest;
import com.example.wippo.domain.auth.social.provider.SocialClient;

@Configuration
@Profile({"dev", "local"})
public class SocialClientStubConfig {

    @Bean
    public SocialClient kakaoStubClient() {
        return stubOf(Provider.KAKAO);
    }

    @Bean
    public SocialClient googleStubClient() {
        return stubOf(Provider.GOOGLE);
    }

    @Bean
    public SocialClient stubOf(Provider provider) {
        return new SocialClient() {
            @Override public Provider provider() { return provider; }

            @Override
            public SocialProfile verify(SocialLoginStartRequest req) {
                String token = (req.accessToken()!=null && !req.accessToken().isBlank())
                    ? req.accessToken() : req.idToken();
                // 정책: nickname만 필수, email은 선택
                return new SocialProfile(
                    provider,
                    "dev-" + (token==null ? "no-token" : token),
                    provider.name().toLowerCase() + "_tester", // nickname
                    "dev@example.com"                           // email (원하면 null)
                );
            }
        };
    }
}