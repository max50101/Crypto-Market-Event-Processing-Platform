package com.example.crypto_platform.market.messaging;

import com.example.crypto_platform.common.event.PriceUpdatedEvent;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MarketEventProducer {
    private final KafkaTemplate<String, PriceUpdatedEvent> kafkaTemplate;
    @Value("${app.kafka.topics.price-updated}")
    private String priceUpdatedTopic;

    public void sendPriceUpdate(PriceUpdatedEvent priceUpdatedEvent){
        kafkaTemplate.send(priceUpdatedTopic,priceUpdatedEvent.symbol(),priceUpdatedEvent);
    }
}
