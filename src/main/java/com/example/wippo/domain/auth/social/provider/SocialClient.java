package com.example.wippo.domain.auth.social.provider;

import com.example.wippo.domain.auth.social.Provider;
import com.example.wippo.domain.auth.social.dto.SocialLoginStartRequest;

public interface SocialClient {
    Provider provider();
    SocialProfile verify(SocialLoginStartRequest req);

    record SocialProfile(
        Provider provider, 
        String providerUserId, 
        String nickname,
        String email
    ) {}
}