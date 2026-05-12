package com.example.crypto_platform.alert.processor;

import com.example.crypto_platform.alert.cache.AlertCache;
import com.example.crypto_platform.alert.entity.Alert;
import com.example.crypto_platform.alert.entity.ConditionType;
import com.example.crypto_platform.alert.service.AlertService;
import com.example.crypto_platform.common.event.PriceUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AlertProcessor {
    private final AlertService alertService;
    private final AlertCache alertCache;

    public void processPriceUpdate(PriceUpdatedEvent priceUpdatedEvent){
        List<Alert> alertList=alertCache.getAlertsBySymbol(priceUpdatedEvent.symbol());
        for(Alert alert: alertList){
            if(isTriggered(alert,priceUpdatedEvent.price())){
                alertService.markTriggered(alert,priceUpdatedEvent);

            }
        }
    }

    private boolean isTriggered(Alert alert, BigDecimal currentPrice){
        if(alert.getConditionType()== ConditionType.ABOVE){
            return currentPrice.compareTo(alert.getTargetPrice())>=0;
        }
        if(alert.getConditionType()== ConditionType.BELOW){
            return currentPrice.compareTo(alert.getTargetPrice())<=0;
        }
        return false;
    }
}
