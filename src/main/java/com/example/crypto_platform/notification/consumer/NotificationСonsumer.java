package com.example.crypto_platform.notification.consumer;

import com.example.crypto_platform.common.event.AlertTriggeredEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationСonsumer {

    @KafkaListener(topics = "${app.kafka.topics.alertTriggered",containerFactory = "alertTriggeredKafkaListenerContainerFactory")
    public void listen(AlertTriggeredEvent event){
        log.info("Alerts was triggered for notification : {}",event);
    }
}
