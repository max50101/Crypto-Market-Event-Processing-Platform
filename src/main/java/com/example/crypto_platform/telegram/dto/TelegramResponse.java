package com.example.crypto_platform.telegram.dto;

import java.util.List;

public record TelegramResponse(boolean ok, List<TelegramUpdate> result) {
}
