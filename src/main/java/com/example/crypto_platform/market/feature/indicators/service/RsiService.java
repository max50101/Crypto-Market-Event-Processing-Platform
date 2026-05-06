package com.example.crypto_platform.market.feature.indicators.service;

import com.example.crypto_platform.market.client.BinanceClient;
import com.example.crypto_platform.market.dto.BinanceKline;
import com.example.crypto_platform.market.feature.common.BinanceKlineCache;
import com.example.crypto_platform.market.feature.common.MarketInterval;
import com.example.crypto_platform.market.feature.common.MarketKlineService;
import com.example.crypto_platform.market.feature.indicators.calculator.RsiCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RsiService {

    private static final int DEFAULT_RSI_PERIOD = 14;
    private static final int DEFAULT_KLINE_LIMIT = 100;
    private static final String DEFAULT_INTERVAL = "15m";

    private final RsiCalculator calculator;
    private final MarketKlineService marketKlineService;

    public BigDecimal calculateRsi(String symbol) {
        return calculateRsi(symbol, DEFAULT_INTERVAL, DEFAULT_RSI_PERIOD);
    }

    public BigDecimal calculateRsi(String symbol, String interval) {
        return calculateRsi(symbol, interval, DEFAULT_RSI_PERIOD);
    }

    public BigDecimal calculateRsi(String symbol, String interval, int period) {


        validatePeriod(period);
        List<BigDecimal> closes = marketKlineService.getKlinesCached(symbol,interval,DEFAULT_KLINE_LIMIT)
                .stream()
                .map(BinanceKline::close)
                .toList();

        return calculator.calculateRsi(closes, period);
    }



    private void validatePeriod(int period) {
        if (period <= 0 || period > 100) {
            throw new IllegalArgumentException("RSI period must be between 1 and 100");
        }
    }


}