package com.example.crypto_platform.market.client;

import com.example.crypto_platform.market.config.BinanceProperties;
import com.example.crypto_platform.market.dto.BinanceKline;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class BinanceClient {

    private final WebClient.Builder binanceWebClient;
    private final BinanceProperties properties;

    public List<BinanceKline> getKlines(String symbol, String interval, int limit) {
        try {
            List<BinanceKline> result = binanceWebClient
                    .baseUrl(properties.getBaseApiUrl())
                    .build()
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/v3/klines")
                            .queryParam("symbol", symbol.trim().toUpperCase())
                            .queryParam("interval", interval)
                            .queryParam("limit", limit)
                            .build())
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<BinanceKline>>() {})
                    .block();

            if (result == null || result.isEmpty()) {
                throw new IllegalStateException("Binance kline response is empty");
            }

            return result;

        } catch (WebClientResponseException e) {
            log.error(
                    "Binance HTTP error. status={}, body={}",
                    e.getStatusCode(),
                    e.getResponseBodyAsString(),
                    e
            );

            throw new IllegalStateException("Failed to get klines from Binance", e);

        } catch (WebClientRequestException e) {
            log.error(
                    "Binance connection error. message={}",
                    e.getMessage(),
                    e
            );

            throw new IllegalStateException("Could not connect to Binance", e);

        }
    }
}

