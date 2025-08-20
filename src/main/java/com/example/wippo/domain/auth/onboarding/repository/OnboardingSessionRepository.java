package com.example.wippo.domain.auth.onboarding.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.wippo.domain.auth.onboarding.entity.OnboardingSession;

public interface OnboardingSessionRepository extends JpaRepository<OnboardingSession, String> {
    
}
