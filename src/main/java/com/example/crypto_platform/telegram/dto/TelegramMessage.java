package com.example.crypto_platform.telegram.dto;

public record TelegramMessage(TelegramUserDto from, TelegramChat chat,String text) {
}
