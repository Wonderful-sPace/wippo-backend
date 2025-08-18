package com.example.wippo.domain.auth.phone;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PhoneVerificationRepository extends JpaRepository<PhoneVerification, Long> {
    Optional<PhoneVerification> findTopByPhoneNumberOrderByIdDesc(String phoneNumber);    
}
