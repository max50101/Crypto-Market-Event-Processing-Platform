package com.example.crypto_platform.alert.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
@Entity
@Table(name="alert_rule")

public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Alert(Long id, String symbol, ConditionType conditionType, AlertStatus status, BigDecimal targetPrice, Long userId) {
        this.id = id;
        this.symbol = symbol;
        this.conditionType = conditionType;
        this.status = status;
        this.targetPrice = targetPrice;
        this.userId=userId;
    }

    public Alert(){

    }


    private String symbol;

    public ConditionType getConditionType() {
        return conditionType;
    }

    public void setConditionType(ConditionType conditionType) {
        this.conditionType = conditionType;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public AlertStatus getStatus() {
        return status;
    }

    public void setStatus(AlertStatus status) {
        this.status = status;
    }

    public BigDecimal getTargetPrice() {
        return targetPrice;
    }

    public void setTargetPrice(BigDecimal targetPrice) {
        this.targetPrice = targetPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Enumerated(EnumType.STRING)
    private ConditionType conditionType;

    @Enumerated(EnumType.STRING)
    private AlertStatus status;
    @Column(name="target_value")
    private BigDecimal targetPrice;



}
