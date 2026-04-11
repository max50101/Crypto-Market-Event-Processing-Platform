package com.example.crypto_platform.market.controller;

import com.example.crypto_platform.market.dto.PriceUpdatedEvent;
import com.example.crypto_platform.market.service.MarketEventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.Instant;

@RestController
@RequestMapping("/api/market")
@RequiredArgsConstructor
public class MarketController {
    private final MarketEventProducer marketEventProducer;

    @GetMapping("/test-price")
    public String sendTestPrice(){
        PriceUpdatedEvent event= new PriceUpdatedEvent("BTCUSDT",new BigDecimal("58500"), Instant.now(),"manual-test");
        marketEventProducer.sendPriceUpdate(event);
        return "Test event sent successfully";
    }



}
