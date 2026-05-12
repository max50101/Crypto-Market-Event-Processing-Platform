package com.example.crypto_platform.market.messaging;

import com.example.crypto_platform.common.event.SubscriptionUpdateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscriptionProducer {
    private final KafkaTemplate<String, SubscriptionUpdateEvent> kafkaTemplate;
    @Value("${app.kafka.topics.subscription-due}")
    private String subscriptionUpdateTopic;

    public void sendSubscriptionUpdate(SubscriptionUpdateEvent event){
        kafkaTemplate.send(subscriptionUpdateTopic,event.subscriptionId().toString(),event);
    }


}
