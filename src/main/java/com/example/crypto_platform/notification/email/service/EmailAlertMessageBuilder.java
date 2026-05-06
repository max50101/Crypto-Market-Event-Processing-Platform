package com.example.crypto_platform.notification.email.service;

import com.example.crypto_platform.common.event.AlertTriggeredEvent;
import org.springframework.stereotype.Service;

@Service
public class EmailAlertMessageBuilder {

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

    public String subject() {
        return "Alert triggered";
    }
}
