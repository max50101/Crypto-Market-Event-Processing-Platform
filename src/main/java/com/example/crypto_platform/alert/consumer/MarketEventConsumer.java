package com.example.crypto_platform.market.service;

import com.example.crypto_platform.market.dto.PriceUpdatedEvent;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class MarketEventConsumer {
    @KafkaListener(topics = "${app.kafka.topics.price-updated}")
    public void listen(PriceUpdatedEvent event){
        log.info("Price: symbol={}, price ={}",event.symbol(),event.price());
    }
}
