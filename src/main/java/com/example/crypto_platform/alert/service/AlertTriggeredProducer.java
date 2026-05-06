package com.example.crypto_platform.alert.service;

import com.example.crypto_platform.common.event.AlertTriggeredEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AlertTriggeredProducer{
    private final KafkaTemplate<String, AlertTriggeredEvent> kafkaTemplate;

    @Value("${app.kafka.topics.alert-triggered}")
    private  String alertTriggeredTopic;

    public void sendAlertTriggeredEvent(AlertTriggeredEvent event){
        this.kafkaTemplate.send(alertTriggeredTopic,event.getSymbol(),event);
    }

}
