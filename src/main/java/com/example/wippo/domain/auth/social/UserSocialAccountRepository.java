package com.example.wippo.domain.auth.social;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSocialAccountRepository extends JpaRepository<UserSocialAccount, Long> {
    // (provider, provider_user_id) 기준으로 유저 계정 검색
    Optional<UserSocialAccount> findByProviderAndProviderUserId(Provider provider, String providerUserId);
}
