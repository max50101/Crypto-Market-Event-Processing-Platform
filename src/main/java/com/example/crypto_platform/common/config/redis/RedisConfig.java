package com.example.crypto_platform.common.config.redis;

import com.example.crypto_platform.telegram.conversation.TelegramConversationState;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, TelegramConversationState> redisTemplateTelegram(
            RedisConnectionFactory cf, ObjectMapper objectMapper
    ){
        RedisTemplate<String,TelegramConversationState> rdl=new RedisTemplate<>();
        Jackson2JsonRedisSerializer<TelegramConversationState> tg=
                new Jackson2JsonRedisSerializer<TelegramConversationState>(objectMapper,TelegramConversationState.class);
        rdl.setKeySerializer(new StringRedisSerializer());
        rdl.setHashKeySerializer(new StringRedisSerializer());
        rdl.setValueSerializer(tg);
        rdl.setHashValueSerializer(tg);
        rdl.setConnectionFactory(cf);
        return rdl;
    }

    @Bean
    public RedisTemplate<String,Object> redisTemplateObject(RedisConnectionFactory cf, ObjectMapper objectMapper){
        RedisTemplate<String,Object> rdl=new RedisTemplate<>();
        GenericJackson2JsonRedisSerializer serializer=
                new GenericJackson2JsonRedisSerializer(objectMapper);
        rdl.setKeySerializer(new StringRedisSerializer());
        rdl.setHashKeySerializer(new StringRedisSerializer());
        rdl.setValueSerializer(serializer);
        rdl.setHashValueSerializer(serializer);
        rdl.setConnectionFactory(cf);
        return rdl;
    }
}
