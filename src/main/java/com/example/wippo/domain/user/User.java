package com.example.wippo.domain.user;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="users", uniqueConstraints=@UniqueConstraint(name="uk_users_phone", columnNames="phone_number"))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name="phone_number", length=32, nullable=false)
    private String phoneNumber;

    @Column(name="display_name", length=80, nullable=false)
    private String displayName;

    @Enumerated(EnumType.STRING)
    @Column(name="status", length=32, nullable=false)
    private AccountStatus status;

    @Column(name="phone_verified_at")
    private LocalDateTime phoneVerifiedAt;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public enum AccountStatus { ACTIVE, SUSPENDED, DELETED }

    // 기본 상태값
    @PrePersist
    void prePersist() {
        if (status == null) status = AccountStatus.ACTIVE;
    }
}
