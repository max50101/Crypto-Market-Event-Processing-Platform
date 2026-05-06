package com.example.crypto_platform.telegram.conversation;

import java.math.BigDecimal;

public record TelegramConversationState(
        TelegramScenario scenario,
        TelegramStep step,
        String symbol,
        BigDecimal price,
        String direction,
        String email
) {
}
