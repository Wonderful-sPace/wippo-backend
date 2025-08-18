package com.example.wippo.domain.user;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    // 전화번호로 유저 찾기 & 없으면 새로 생성
    public User getOrCreateActiveByPhoneNumber(String phoneE164) {
        return userRepository.findByPhoneNumber(phoneE164)
            .orElseGet(() -> userRepository.save(
                User.builder()
                    .phoneNumber(phoneE164)
                    .status(User.AccountStatus.ACTIVE)
                    .phoneVerifiedAt(LocalDateTime.now())
                    .build()
            ));
    }

    // 전화번호 인증 완료 표시
    public void markPhoneVerified(User user) {
        user.setPhoneVerifiedAt(LocalDateTime.now());
        userRepository.save(user);
    }
    
}
