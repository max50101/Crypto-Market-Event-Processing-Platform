package com.example.crypto_platform.notification.email.telegram_email.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "telegram_email_verification_tokens")
@NoArgsConstructor
public class EmailVerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id",nullable = false)
    private Long userId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }


    @Column(name="token",unique = true,nullable = false)
    private String token;

    @Column(name="expires_at",nullable = false)
    private Instant expiresAt;

    @Column(name = "used",nullable = false)
    private boolean used;
}
