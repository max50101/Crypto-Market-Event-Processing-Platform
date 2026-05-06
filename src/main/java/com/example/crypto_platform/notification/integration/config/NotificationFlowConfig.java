package com.example.crypto_platform.notification.integration.config;

import com.example.crypto_platform.notification.email.EmailSender;
import com.example.crypto_platform.notification.email.service.EmailAlertNotificationSender;
import com.example.crypto_platform.notification.telegram.TelegramNotificationSender;
import com.example.crypto_platform.user.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;

@Configuration
public class NotificationFlowConfig {

    @Bean
    public IntegrationFlow alertTriggeredFlow(EmailAlertNotificationSender emailAlertNotificationSender, TelegramNotificationSender telegramNotificationSender, UserService service){
        return IntegrationFlow
                .from("alertTriggeredChannel")
                //.handle(sender,"send")
                .publishSubscribeChannel(sub-> sub
                        .subscribe(flow-> flow.handle(telegramNotificationSender,"send"))
                        .subscribe(flow->flow.handle(emailAlertNotificationSender,"send")))
                .get();

    }
}
