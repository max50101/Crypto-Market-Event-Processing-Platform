package com.example.crypto_platform.market.feature.currentprice;

import com.example.crypto_platform.common.exception.ResourseNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CurrentPriceQuarryService {
    private final CurrentPriceRepository repo;
    public BigDecimal getCurrentPrice(String symbol){
        return repo.findById(symbol.toUpperCase())
                .orElseThrow(()->new ResourseNotFoundException("No such symbol in database"))
                .getPrice();
    }
}
