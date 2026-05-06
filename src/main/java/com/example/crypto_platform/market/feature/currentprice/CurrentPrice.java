package com.example.crypto_platform.market.feature.currentprice;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "current_prices")
@NoArgsConstructor
public class CurrentPrice {

    @Id
    @Column(name="symbol",nullable = false,length = 60)
    private String symbol;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(name="updated_at",nullable = false)
    private Instant updatedAt;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
