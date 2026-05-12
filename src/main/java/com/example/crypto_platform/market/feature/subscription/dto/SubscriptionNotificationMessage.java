package com.example.crypto_platform.market.feature.subscription.dto;

public record SubscriptionNotificationMessage (
    Long subscriptionId,
    Long userId,
    Long telegramChatId,
    String email,
    String symbol,
    String interval,
    byte[] chartImage,
    String filename,
    String caption,
    boolean sendTelegram,
    boolean sendEmail
){}
