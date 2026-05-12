package com.example.crypto_platform.market.feature.common;

import com.example.crypto_platform.market.client.BinanceClient;
import com.example.crypto_platform.market.dto.BinanceKline;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MarketKlineService {
    private final BinanceClient client;
    private final BinanceKlineCache binanceKlineCache;

    public List<BinanceKline> getKlinesCached(
            String normalizedSymbol,
            String interval,
            int limit
    ) {

        return binanceKlineCache.get(normalizedSymbol, interval, limit)
                .orElseGet(() -> {
                    log.info("Using binance client for getting klines");
                    List<BinanceKline> klines = client.getKlines(
                            normalizedSymbol,
                            interval,
                            limit
                    );

                    binanceKlineCache.save(
                            normalizedSymbol,
                            interval,
                            limit,
                            klines
                    );

                    return klines;
                });
    }
    private String normalizeSymbol(String symbol) {
        if (symbol == null || symbol.isBlank()) {
            throw new IllegalArgumentException("Symbol is required");
        }

        return symbol.trim().toUpperCase();
    }
}
