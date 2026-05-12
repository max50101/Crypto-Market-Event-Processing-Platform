package com.example.crypto_platform.common.event;

import java.math.BigDecimal;
import java.time.Instant;

public record PriceUpdatedEvent(
        String symbol,
        BigDecimal price,
        Instant eventTime,
        String sourсe
) {
}
