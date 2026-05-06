package com.example.crypto_platform.common.config.kafka;

import com.example.crypto_platform.common.event.AlertTriggeredEvent;
import com.example.crypto_platform.common.event.PriceUpdatedEvent;
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
        Map<String,Object> props=new HashMap<>(kafkaProperties.buildConsumerProperties());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        JsonDeserializer<PriceUpdatedEvent> jsonDeserializer=new JsonDeserializer<>(PriceUpdatedEvent.class);
        jsonDeserializer.addTrustedPackages("com.example.crypto_platform.market");
        jsonDeserializer.setUseTypeHeaders(false);
        return new DefaultKafkaConsumerFactory<>(props,new StringDeserializer(),jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String,PriceUpdatedEvent> priceUpdatedEventConcurrentKafkaListenerContainerFactory(
        ConsumerFactory<String,PriceUpdatedEvent> priceUpdatedEventConsumerFactory
    ){
        ConcurrentKafkaListenerContainerFactory<String,PriceUpdatedEvent> factory=new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(priceUpdatedEventConsumerFactory);
        return factory;
    }

    @Bean
    public ConsumerFactory<String, AlertTriggeredEvent> alertTriggeredEventConsumerFactory(KafkaProperties kafkaProperties){
        Map<String,Object> props=new HashMap<>(kafkaProperties.buildConsumerProperties());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        JsonDeserializer<AlertTriggeredEvent> jsonDeserializer=new JsonDeserializer<>(AlertTriggeredEvent.class);
        jsonDeserializer.addTrustedPackages("com.example.crypto_platform");
        jsonDeserializer.setUseTypeHeaders(false);
        return new DefaultKafkaConsumerFactory<>(props,new StringDeserializer(),jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String,AlertTriggeredEvent> alertTriggeredKafkaListenerContainerFactory(
            ConsumerFactory<String,AlertTriggeredEvent> alertTriggeredEventConsumerFactory
    ){
        ConcurrentKafkaListenerContainerFactory<String,AlertTriggeredEvent> factory=new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(alertTriggeredEventConsumerFactory);
        return factory;
    }
}
