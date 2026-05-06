package com.example.crypto_platform.market.feature.currentprice;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.Instant;

public interface CurrentPriceRepository extends JpaRepository<CurrentPrice,String> {

    @Modifying
    @Transactional
    @Query(value = """
             INSERT INTO current_prices(symbol,price,updated_at)
             VALUES (:symbol,:price,:updated_at)
             ON CONFLICT(symbol)
             DO UPDATE SET
                price = EXCLUDED.price,
                updated_at = EXCLUDED.updated_at
            """,nativeQuery = true)
    void upsert(@Param("symbol")String symbol, @Param("price")BigDecimal price, @Param("updated_at")Instant updateAt);
}
