package com.example.crypto_platform.market.feature.currentprice;

import com.example.crypto_platform.common.event.PriceUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class CurrentPriceProjectionService{
    private final CurrentPriceRepository repository;
    private final JdbcTemplate jdbcTemplate;

    public void updatePrice(PriceUpdatedEvent event){
        jdbcTemplate.update("""
                insert into current_prices(symbol, price, updated_at)
                values (?,?,?)
                on conflict(symbol) do update
                set price =excluded.price,
                    updated_at=excluded.updated_at
                """,event.symbol(),event.price(), Timestamp.from(event.eventTime()));
    }
}
