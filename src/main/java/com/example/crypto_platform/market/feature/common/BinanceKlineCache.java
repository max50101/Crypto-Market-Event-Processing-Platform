package com.example.crypto_platform.market.feature.common;

import com.example.crypto_platform.market.dto.BinanceKline;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BinanceKlineCache {
    private final RedisTemplate<String,Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private static final Duration TTL=Duration.ofMinutes(1);
    public Optional<List<BinanceKline>> get(String symbol, String interval, int limit){
        Object value=redisTemplate.opsForValue()
                .get(buildKey(
                        symbol,
                        interval,
                        limit));
        if(value==null){
            return Optional.empty();
        }
        List<BinanceKline> klines=objectMapper.convertValue(value, new TypeReference<List<BinanceKline>>() {});
        return Optional.of(klines);
    }

    public void save(String symbol, String interval, int limit, List<BinanceKline> klines){
        redisTemplate.opsForValue()
                .set(buildKey(symbol, interval, limit),
                        klines,
                        TTL);
    }
    private String buildKey(String symbol, String interval, int limit){
        return "binance-klines:"+symbol+":"+interval+";"+limit;
    }
}
