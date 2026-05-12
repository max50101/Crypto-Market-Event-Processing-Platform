package com.example.crypto_platform.notification.integration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;

@Configuration
public class IntegrationConfig {

    @Bean
    public MessageChannel alertTriggeredChannel(){
        return new DirectChannel();
    }

    @Bean
    public MessageChannel subscriptionDueChannel(){return new DirectChannel();}
}
