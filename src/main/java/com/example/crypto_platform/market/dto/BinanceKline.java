package com.example.crypto_platform.market.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.math.BigDecimal;
@JsonFormat(shape = JsonFormat.Shape.ARRAY)
@JsonPropertyOrder({
        "openTime",
        "open",
        "high",
        "low",
        "close",
        "volume",
        "closeTime",
        "assetVolume",
        "numberOfTrades",
        "takerBuyBaseAssetVolume",
        "takerBuyQuoteAssetVolume",
        "ignore"
})


@JsonIgnoreProperties(ignoreUnknown = true)
public record BinanceKline(
        Long openTime,
        BigDecimal open,
        BigDecimal high,
        BigDecimal low,

        BigDecimal close,

        BigDecimal volume,

        Long closeTime,

        BigDecimal assetVolume,

        Long numberOfTrades,
        BigDecimal takerBuyBaseAssetVolume,
        BigDecimal takerBuyQuoteAssetVolume,
        String ignore


) {
}
