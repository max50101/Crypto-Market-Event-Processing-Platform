package com.example.crypto_platform.market.feature.currentprice;

import com.example.crypto_platform.common.event.PriceUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CurrentPriceConsumer {
    private final CurrentPriceProjectionService priceProjectionService;
    @KafkaListener(topics = "${app.kafka.topics.price-updated}",groupId = "current-price-projection",containerFactory = "priceUpdatedEventConcurrentKafkaListenerContainerFactory")
    public void listen(PriceUpdatedEvent event){

        priceProjectionService.updatePrice(event);
    }
}
