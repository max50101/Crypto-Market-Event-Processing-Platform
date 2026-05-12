package com.example.crypto_platform.notification.integration.config;

import com.example.crypto_platform.market.feature.subscription.dto.SubscriptionNotificationMessage;
import com.example.crypto_platform.market.feature.subscription.service.ChartSubscriptionScheduleService;
import com.example.crypto_platform.market.feature.subscription.service.SubscriptionNotificationMessageBuilder;
import com.example.crypto_platform.notification.email.service.EmailNotificationSender;
import com.example.crypto_platform.notification.telegram.TelegramNotificationSender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;

@Configuration
public class NotificationFlowConfig {

    @Bean
    public IntegrationFlow alertTriggeredFlow(EmailNotificationSender emailNotificationSender, TelegramNotificationSender telegramNotificationSender){
        return IntegrationFlow
                .from("alertTriggeredChannel")
                .publishSubscribeChannel(sub-> sub
                        .subscribe(flow-> flow.handle(telegramNotificationSender,"send"))
                        .subscribe(flow->flow.handle(emailNotificationSender,"send")))
                .get();
    }

    @Bean
    public IntegrationFlow subscriptionDueFlow(EmailNotificationSender emailNotificationSender,
                                               TelegramNotificationSender telegramNotificationSender,
                                               SubscriptionNotificationMessageBuilder notificationMessageService,
                                               ChartSubscriptionScheduleService chartSubscriptionScheduleService){
        return IntegrationFlow
                .from("subscriptionDueChannel")
                .transform(notificationMessageService,"build")
                .publishSubscribeChannel(sub-> sub

                        .subscribe(flow-> flow.filter("payload.sendTelegram")
                                .handle(telegramNotificationSender,"send"))

                        .subscribe(flow->flow.filter("payload.sendEmail")
                                .handle(emailNotificationSender,"send")))

                .handle((payload, headers) -> {
                    SubscriptionNotificationMessage message =
                            (SubscriptionNotificationMessage) payload;

                    chartSubscriptionScheduleService.markSent(message.subscriptionId());

                    return null;
                })
                .get();
    }
}
