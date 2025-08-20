package com.example.wippo.domain.auth.phone.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
  name = "phone_verifications",
  indexes = {
    @Index(name="ix_phone_verif_phone_created", columnList = "phone_number, created_at"),
    @Index(name="ix_phone_verif_expires_at", columnList = "expires_at")
  }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class PhoneVerification {
    @Id 
    @GeneratedValue(strategy=GenerationType.IDENTITY) 
    private Long id;
    
    @Column(name="phone_number", length=32, nullable=false) 
    private String phoneNumber;
    
    @Column(name="code_hash", length=64, nullable=false) 
    private String codeHash;

    @Column(name="salt", length=64, nullable=false)
    private String salt;   // 행별 랜덤

    @Column(name="attempt_count", nullable=false) 
    private int attemptCount;

    @CreatedDate 
    @Column(name="created_at", updatable=false, nullable=false) 
    private LocalDateTime createdAt;
    
    @Column(name="expires_at", nullable=false) 
    private LocalDateTime expiresAt;

    @Version
    private Long version;  // 낙관적 락(선택)

    public boolean isExpired() { 
        return LocalDateTime.now().isAfter(expiresAt); 
    }
}
