package com.example.crypto_platform.market.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record PriceUpdatedEvent(
        String symbol,
        BigDecimal price,
        Instant eventTime,
        String sourсe
) {
}
