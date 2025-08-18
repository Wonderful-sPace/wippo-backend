package com.example.wippo.domain.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPhoneNumber(String phoneNumber);
    Optional<User> findByIdAndStatus(Long id, User.AccountStatus status);

    boolean existsByPhoneNumber(String phoneNumber);
}
