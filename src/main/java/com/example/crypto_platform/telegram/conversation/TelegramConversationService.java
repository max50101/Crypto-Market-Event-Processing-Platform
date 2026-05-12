package com.example.crypto_platform.telegram.conversation;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TelegramConversationService {
    private final RedisTemplate<String,TelegramConversationState> rdTemplate;
    private static final Duration TTL= Duration.ofMinutes(15);
    private final ObjectMapper mapper;

    public void save(Long telegramUserId, TelegramConversationState state){
        String key=buildTemplate(telegramUserId);
        rdTemplate.opsForValue().set(key,state);
    }

    public Optional<TelegramConversationState> get(Long telegramUserId){
        String key=buildTemplate(telegramUserId);
        TelegramConversationState telegramConversationState=rdTemplate.opsForValue().get(key);
        return Optional.ofNullable(telegramConversationState);
    }

    public void delete(Long telegramUserId){
        rdTemplate.delete(buildTemplate(telegramUserId));
    }

    private String buildTemplate(Long telegramUserId){
        return "telegram:conversation:"+telegramUserId;
    }
}
