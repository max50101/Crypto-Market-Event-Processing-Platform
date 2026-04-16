package com.example.crypto_platform.common.event;

import com.example.crypto_platform.alert.entity.ConditionType;

import java.math.BigDecimal;

public class AlertTrigeredEvent {
    private Long alertId;
    private Long userId;

    private String symbol;
    private ConditionType conditionType;
    private BigDecimal triggeredPrice;
    private BigDecimal targetPrice;
}
