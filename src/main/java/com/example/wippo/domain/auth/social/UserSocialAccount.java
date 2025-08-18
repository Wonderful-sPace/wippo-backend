package com.example.wippo.domain.auth.social;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.wippo.domain.user.User;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="user_social_accounts", uniqueConstraints=@UniqueConstraint(name="uk_provider_uid", columnNames={"provider","provider_user_id"}))
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class UserSocialAccount {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY) 
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name="provider", length=16, nullable=false)
    private Provider provider;  // GOOGLE, KAKAO

    @Column(name="provider_user_id", length=191, nullable=false)
    private String providerUserId;

    @Column(name="email", length=191)
    private String email;

    @CreatedDate
    @Column(name="created_at", updatable=false)
    private LocalDateTime createdAt;
}
