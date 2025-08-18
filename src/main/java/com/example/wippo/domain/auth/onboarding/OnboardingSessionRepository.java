package com.example.wippo.domain.auth.onboarding;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OnboardingSessionRepository extends JpaRepository<OnboardingSession, String> {
    
}
