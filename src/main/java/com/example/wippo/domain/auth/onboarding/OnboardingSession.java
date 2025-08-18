package com.example.wippo.domain.auth.onboarding;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.wippo.domain.auth.social.Provider;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="onboarding_sessions")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class OnboardingSession {
    @Id
    @Column(length=36)
    private String id;
    
    @Enumerated(EnumType.STRING) 
    @Column(name="provider", length=16, nullable=false)
    private Provider provider;

    @Column(name="provider_user_id", length=191, nullable=false)
    private String providerUserId;

    @Column(name="email", length=191) 
    private String email;

    @Column(name="suggested_display_name", length=80) 
    private String suggestedDisplayName;

    @Column(name="phone_number", length=32) 
    private String phoneNumber;

    @CreatedDate 
    @Column(name="created_at", updatable=false) 
    private LocalDateTime createdAt;

    @Column(name="expires_at", nullable=false) 
    private LocalDateTime expiresAt;

    public boolean isExpired() { 
        return LocalDateTime.now().isAfter(expiresAt); 
    }
}