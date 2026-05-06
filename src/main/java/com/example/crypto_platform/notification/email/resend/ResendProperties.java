package com.example.crypto_platform.notification.email.resend;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "resend")
public record ResendProperties(String apiKey) {
}
