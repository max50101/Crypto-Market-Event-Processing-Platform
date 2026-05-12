package com.example.crypto_platform.notification.email.service;

import com.example.crypto_platform.common.event.AlertTriggeredEvent;
import com.example.crypto_platform.market.feature.subscription.dto.SubscriptionNotificationMessage;
import org.springframework.stereotype.Service;

@Service
public class EmailMessageBuilder {

    public String build(AlertTriggeredEvent event) {
        return """
                Alert %s triggered
                
                Target price: %s
                Current price: %s
                """
                .formatted(
                        event.getAlertId(),
                        event.getTargetPrice(),
                        event.getTriggeredPrice()
                );
    }

    public String subjectAlert() {
        return "Alert triggered";
    }
    public String subjectSubscription(){ return "Subscription update";};

}
