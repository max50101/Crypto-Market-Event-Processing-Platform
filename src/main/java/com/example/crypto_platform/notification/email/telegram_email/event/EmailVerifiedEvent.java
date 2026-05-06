package com.example.crypto_platform.notification.email.telegram_email.event;

public record EmailVerifiedEvent(Long chatId, String email) {
}
