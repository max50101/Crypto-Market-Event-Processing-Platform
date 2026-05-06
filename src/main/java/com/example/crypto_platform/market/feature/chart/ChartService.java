package com.example.crypto_platform.market.feature.chart;

import com.example.crypto_platform.market.client.BinanceClient;
import com.example.crypto_platform.market.dto.BinanceKline;
import com.example.crypto_platform.market.feature.common.BinanceKlineCache;
import com.example.crypto_platform.market.feature.common.MarketInterval;
import com.example.crypto_platform.market.feature.common.MarketKlineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChartService {
    private final BinanceClient client;
    private static final int DEFAULT_KLINE_LIMIT = 100;
    private final MarketKlineService marketKlineService;
    private final XChartGenerator xChartGenerator;

    public byte[] generatePriceChart(String symbol, String interval) {
        List<BinanceKline> klineList = marketKlineService
                .getKlinesCached(symbol, interval, DEFAULT_KLINE_LIMIT);
        return xChartGenerator.generatePriceChart(klineList, symbol, interval);
    }


}
