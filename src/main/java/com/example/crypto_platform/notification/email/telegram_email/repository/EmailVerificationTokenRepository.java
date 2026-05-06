package com.example.crypto_platform.notification.email.telegram_email.repository;

import com.example.crypto_platform.notification.email.telegram_email.entity.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken,Long> {
    Optional<EmailVerificationToken> findByToken(String toke);
}
