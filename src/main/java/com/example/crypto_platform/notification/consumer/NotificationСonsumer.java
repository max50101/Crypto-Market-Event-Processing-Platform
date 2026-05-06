package com.example.crypto_platform.notification.consumer;

import com.example.crypto_platform.common.event.AlertTriggeredEvent;
import com.example.crypto_platform.notification.integration.NotificationGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationСonsumer {
    private final NotificationGateway gateway;

    public NotificationСonsumer(NotificationGateway gateway) {
        this.gateway = gateway;
    }

    @KafkaListener(topics = "${app.kafka.topics.alert-triggered}", containerFactory = "alertTriggeredKafkaListenerContainerFactory")
    public void listen(AlertTriggeredEvent event) {
        gateway.sendNotification(event);
        log.info("Alerts was triggered for notification : {}", event);
    }
}
