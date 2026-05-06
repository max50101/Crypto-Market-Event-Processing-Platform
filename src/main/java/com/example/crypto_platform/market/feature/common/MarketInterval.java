package com.example.crypto_platform.market.feature.common;

import java.util.Arrays;

public enum MarketInterval {
    ONE_MINUTE("1m"),
    THREE_MINUTES("3m"),
    FIVE_MINUTES("5m"),
    FIFTEEN_MINUTES("15m"),
    THIRTY_MINUTES("30m"),

    ONE_HOUR("1h"),
    TWO_HOURS("2h"),
    FOUR_HOURS("4h"),
    SIX_HOURS("6h"),
    EIGHT_HOURS("8h"),
    TWELVE_HOURS("12h"),

    ONE_DAY("1d"),
    THREE_DAYS("3d"),
    ONE_WEEK("1w"),
    ONE_MONTH("1M");

    private final String value;

    MarketInterval(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static String fromValue(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Interval is required");
        }

        return Arrays.stream(values())
                .filter(interval -> interval.value.equals(value))
                .map(it->it.value)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported interval: " + value));
    }
}
