package com.example.crypto_platform.notification.consumer.integration;

import com.example.crypto_platform.common.event.AlertTriggeredEvent;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway
public interface NotificationGateway {

    @Gateway(requestChannel="notificationChannel")
     void sendNotification(AlertTriggeredEvent event);

}
