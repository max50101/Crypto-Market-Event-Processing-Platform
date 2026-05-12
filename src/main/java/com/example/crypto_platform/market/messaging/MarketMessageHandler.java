package com.example.crypto_platform.market.messaging;

import com.example.crypto_platform.common.event.PriceUpdatedEvent;
import com.example.crypto_platform.market.dto.external.BinanceTradeMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;

@Slf4j
@Service
public class MarketMessageHandler {
    @Autowired
    private MarketEventProducer kafkaProducer;
    private final ObjectMapper objectMapper;
    public MarketMessageHandler(ObjectMapper objectMapper){
        this.objectMapper=objectMapper;
    }

    public void handle(String payload){
        try{
            BinanceTradeMessage message=objectMapper.readValue(payload, BinanceTradeMessage.class);
            if(message.getData()==null){
                log.error("Recieved message without data {}",payload);
                return;
            }
            String symbol=message.getData().getS();
            String priceRaw=message.getData().getP();
            if(symbol==null||priceRaw==null){
                log.error("Recieved empty symbol price s={}, p={}",symbol,priceRaw);
                return;
            }
            BigDecimal price=new BigDecimal(priceRaw);
            PriceUpdatedEvent priceUpdatedEvent=new PriceUpdatedEvent(symbol,price, Instant.now(),"Binance");
            kafkaProducer.sendPriceUpdate(priceUpdatedEvent);
        }catch (Exception e){
            log.error("Failed to process binance trade-dto payload:{}",payload,e);
        }
    }
}
