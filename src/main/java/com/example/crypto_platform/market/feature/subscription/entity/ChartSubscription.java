package com.example.crypto_platform.market.feature.subscription.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "chart_subscriptions")
@NoArgsConstructor
public class ChartSubscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="user_id",nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String symbol;

    @Column(nullable = false)
    private String interval;

    @Column(name="frequency_minutes",nullable = false)
    private Integer frequencyMinutes;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SubscriptionStatus status;

    @Column(name="send_telegram",nullable = false)
    private boolean sendTelegram;

    @Column(name="send_email",nullable = false)
    private boolean sendEmail;

    @Column(name="created_at",nullable = false)
    private Instant createdAt;

    @Column(name="next_send_at",nullable = false)
    private Instant nextSendAt;

    @Column(name="last_sent_at")
    private Instant lastSentAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser_id() {
        return userId;
    }

    public void setUser_id(Long userId) {
        this.userId = userId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public Integer getFrequencyMinutes() {
        return frequencyMinutes;
    }

    public void setFrequencyMinutes(Integer frequencyMinutes) {
        this.frequencyMinutes = frequencyMinutes;
    }

    public boolean isSendTelegram() {
        return sendTelegram;
    }

    public void setSendTelegram(boolean sendTelegram) {
        this.sendTelegram = sendTelegram;
    }

    public SubscriptionStatus getStatus() {
        return status;
    }

    public void setStatus(SubscriptionStatus status) {
        this.status = status;
    }

    public boolean isSendEmail() {
        return sendEmail;
    }

    public void setSendEmail(boolean sendEmail) {
        this.sendEmail = sendEmail;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getNextSendAt() {
        return nextSendAt;
    }

    public void setNextSendAt(Instant nextSendAt) {
        this.nextSendAt = nextSendAt;
    }

    public Instant getLastSentAt() {
        return lastSentAt;
    }

    public void setLastSentAt(Instant lastSentAt) {
        this.lastSentAt = lastSentAt;
    }
}
