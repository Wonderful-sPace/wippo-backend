package com.example.wippo.domain.auth.social.provider;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.wippo.domain.auth.social.Provider;
import com.example.wippo.domain.auth.social.dto.SocialLoginStartRequest;

import lombok.Data;

@Service
@Profile("!dev & !local & !test")
public class KakaoClient implements SocialClient {
    private final WebClient web = WebClient.builder()
            .baseUrl("https://kapi.kakao.com")
            .build();

    @Override public Provider provider() { return Provider.KAKAO; }

    @Override
    public SocialProfile verify(SocialLoginStartRequest req) {
        if (req.accessToken() == null || req.accessToken().isBlank()) {
            throw new IllegalArgumentException("Kakao requires accessToken");
        }

        KakaoMe me = web.get().uri("/v2/user/me")
                .headers(h -> h.setBearerAuth(req.accessToken()))
                .retrieve()
                .onStatus(HttpStatusCode::isError, rsp ->
                        rsp.bodyToMono(String.class)
                           .map(body -> new IllegalArgumentException("Kakao verify failed: " + body)))
                .bodyToMono(KakaoMe.class)
                .block();

        String id     = String.valueOf(me.id);
        String nickname   = (me.kakao_account != null && me.kakao_account.profile != null) ? me.kakao_account.profile.nickname : null;
        if (nickname == null || nickname.isBlank()) nickname = "kakao_" + id;
        String email  = (me.kakao_account != null) ? me.kakao_account.email : null;

        return new SocialProfile(Provider.KAKAO, id, nickname, email);
    }

    // ===== Kakao response DTO (필요 필드만) =====
    @Data static class KakaoMe { Long id; KakaoAccount kakao_account; }
    @Data static class KakaoAccount { String email; KakaoProfile profile; }
    @Data static class KakaoProfile { String nickname; }
}