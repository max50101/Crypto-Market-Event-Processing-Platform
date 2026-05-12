package com.example.crypto_platform.telegram.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class TelegramConfig {
    private final TelegramProperties telegramProperties;

    @Bean
    public WebClient telegramWebClient(WebClient.Builder builder){
        return builder.baseUrl(telegramProperties.buildBotBaseUrl()).build();
    }
}
