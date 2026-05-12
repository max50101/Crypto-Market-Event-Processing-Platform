package com.example.crypto_platform.alert.consumer;

import com.example.crypto_platform.alert.processor.AlertProcessor;
import com.example.crypto_platform.alert.service.AlertService;
import com.example.crypto_platform.common.event.PriceUpdatedEvent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class MarketEventConsumer {
    private final AlertProcessor alertProcessor;
    public MarketEventConsumer(AlertProcessor alertService){
        this.alertProcessor=alertService;
    }
    @KafkaListener(topics = "${app.kafka.topics.price-updated}",groupId = "alert-processor",containerFactory = "priceUpdatedEventConcurrentKafkaListenerContainerFactory")
    public void listen(PriceUpdatedEvent event){
        alertProcessor.processPriceUpdate(event);
    }
}
