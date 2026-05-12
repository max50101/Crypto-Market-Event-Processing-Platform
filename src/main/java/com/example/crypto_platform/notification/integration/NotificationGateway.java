package com.example.crypto_platform.notification.integration;

import com.example.crypto_platform.common.event.AlertTriggeredEvent;
import com.example.crypto_platform.common.event.SubscriptionUpdateEvent;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway
public interface NotificationGateway {
    @Gateway(requestChannel="alertTriggeredChannel")
     void sendNotification(AlertTriggeredEvent event);

    @Gateway(requestChannel = "subscriptionDueChannel")
    void sendNotification(SubscriptionUpdateEvent event);

}
