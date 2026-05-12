package com.example.crypto_platform.common.config.kafka;

import com.example.crypto_platform.common.event.AlertTriggeredEvent;
import com.example.crypto_platform.common.event.PriceUpdatedEvent;
import com.example.crypto_platform.common.event.SubscriptionUpdateEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {
    @Bean
    public ConsumerFactory<String, PriceUpdatedEvent> priceUpdatedEventConsumerFactory(KafkaProperties kafkaProperties){

        return consumerFactory(kafkaProperties,PriceUpdatedEvent.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String,PriceUpdatedEvent> priceUpdatedEventConcurrentKafkaListenerContainerFactory(
        ConsumerFactory<String,PriceUpdatedEvent> priceUpdatedEventConsumerFactory
    ){
        return listenerFactory(priceUpdatedEventConsumerFactory);
    }

    @Bean
    public ConsumerFactory<String, AlertTriggeredEvent> alertTriggeredEventConsumerFactory(KafkaProperties kafkaProperties){
        return consumerFactory(kafkaProperties,AlertTriggeredEvent.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String,AlertTriggeredEvent> alertTriggeredKafkaListenerContainerFactory(
            ConsumerFactory<String,AlertTriggeredEvent> alertTriggeredEventConsumerFactory
    ){
        return listenerFactory(alertTriggeredEventConsumerFactory);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, SubscriptionUpdateEvent> subscriptionUpdateConcurrentKafkaListenerContainerFactory(
        ConsumerFactory<String,SubscriptionUpdateEvent> subscriptionUpdateEventConsumerFactory
    ){
        return listenerFactory(subscriptionUpdateEventConsumerFactory);
    }

    @Bean
    public ConsumerFactory<String,SubscriptionUpdateEvent> subscriptionUpdateEventConsumerFactory(
        KafkaProperties kafkaProperties
    ){
        return consumerFactory(kafkaProperties,SubscriptionUpdateEvent.class);
    }




    private <T> ConsumerFactory<String,T> consumerFactory(KafkaProperties properties, Class<T> eventClass){
        Map<String,Object> props=new HashMap<>(properties.buildConsumerProperties());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        JsonDeserializer<T> jsonDeserializer=new JsonDeserializer<>(eventClass);
        jsonDeserializer.addTrustedPackages("com.example.crypto_platform");
        jsonDeserializer.setUseTypeHeaders(false);
        return new DefaultKafkaConsumerFactory<>(props,new StringDeserializer(),jsonDeserializer);
    }

    private <T> ConcurrentKafkaListenerContainerFactory<String,T> listenerFactory(
            ConsumerFactory<String,T> consumerFactory){
        ConcurrentKafkaListenerContainerFactory<String,T> factory=new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
}
