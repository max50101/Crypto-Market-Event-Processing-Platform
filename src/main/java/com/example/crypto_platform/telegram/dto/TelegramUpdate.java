package com.example.crypto_platform.telegram.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TelegramUpdate(@JsonProperty("update_id") Long updateId, TelegramMessage message,
                             @JsonProperty("callback_query") TelegramCallbackQuarry callbackQuarry) {
}
