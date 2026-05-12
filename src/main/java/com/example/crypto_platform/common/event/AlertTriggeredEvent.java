package com.example.crypto_platform.common.event;

import com.example.crypto_platform.alert.entity.ConditionType;

import java.math.BigDecimal;
import java.time.Instant;

public class AlertTriggeredEvent {
    private Long alertId;
    private Long userId;

    private String symbol;
    private ConditionType conditionType;

    public Long getAlertId() {
        return alertId;
    }

    public void setAlertId(Long alertId) {
        this.alertId = alertId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public ConditionType getConditionType() {
        return conditionType;
    }

    public void setConditionType(ConditionType conditionType) {
        this.conditionType = conditionType;
    }

    public BigDecimal getTriggeredPrice() {
        return triggeredPrice;
    }

    public void setTriggeredPrice(BigDecimal triggeredPrice) {
        this.triggeredPrice = triggeredPrice;
    }

    public BigDecimal getTargetPrice() {
        return targetPrice;
    }

    public void setTargetPrice(BigDecimal targetPrice) {
        this.targetPrice = targetPrice;
    }

    public Instant getTriggeredAt() {
        return triggeredAt;
    }

    public void setTriggeredAt(Instant triggeredAt) {
        this.triggeredAt = triggeredAt;
    }

    private BigDecimal triggeredPrice;
    private BigDecimal targetPrice;
    private Instant triggeredAt;
    public AlertTriggeredEvent(){

    }
    public AlertTriggeredEvent(Long alertId, Long userId, String symbol,
                               ConditionType conditionType, BigDecimal triggeredPrice,
                               BigDecimal targetPrice, Instant triggeredAt) {
        this.alertId = alertId;
        this.userId = userId;
        this.symbol = symbol;
        this.conditionType = conditionType;
        this.triggeredPrice = triggeredPrice;
        this.targetPrice = targetPrice;
        this.triggeredAt = triggeredAt;
    }
}
