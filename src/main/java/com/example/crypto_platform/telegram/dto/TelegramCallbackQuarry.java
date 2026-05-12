package com.example.crypto_platform.telegram.dto;

public record TelegramCallbackQuarry(Long id, TelegramUserDto from, TelegramMessage message, String data ){
}
