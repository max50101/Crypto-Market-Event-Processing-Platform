package com.example.crypto_platform.alert.service;

import com.example.crypto_platform.alert.cache.AlertCache;
import com.example.crypto_platform.alert.entity.Alert;
import com.example.crypto_platform.alert.entity.AlertStatus;
import com.example.crypto_platform.alert.entity.ConditionType;
import com.example.crypto_platform.alert.repository.AlertRepository;
import com.example.crypto_platform.common.event.AlertTriggeredEvent;
import com.example.crypto_platform.common.event.PriceUpdatedEvent;
import com.example.crypto_platform.common.exception.ResourseNotFoundException;
import jakarta.transaction.Transactional;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AlertService {
    private final AlertRepository alertRepository;
    private final AlertCache alertCache;
    private final AlertTriggeredProducer triggeredProducer;

    public AlertService(AlertRepository alertRepository, AlertCache alertCache, AlertTriggeredProducer producer) {
        this.alertRepository = alertRepository;
        this.alertCache = alertCache;
        this.triggeredProducer=producer;
    }

    @Transactional
    public void markTriggered(Alert alert, PriceUpdatedEvent priceUpdatedEvent) {
        Alert dbAlert = alertRepository.findById(alert.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Alert not found"));

        if (dbAlert.getStatus() == AlertStatus.TRIGGERED) {
            return;
        }
        dbAlert.setStatus(AlertStatus.TRIGGERED);
        alertRepository.save(dbAlert);
        alertCache.removeAlert(dbAlert.getId());
        AlertTriggeredEvent alertTriggeredEvent = new AlertTriggeredEvent(alert.getId(), alert.getUserId(),
                alert.getSymbol(),alert.getConditionType(), priceUpdatedEvent.price(), alert.getTargetPrice(),
                priceUpdatedEvent.eventTime());
        triggeredProducer.sendAlertTriggeredEvent(alertTriggeredEvent);
    }


    public Page<Alert> getAlerts(AlertStatus status, String symbol, Pageable pageable) {
        if (status != null && symbol != null) {
            return alertRepository.findBySymbolAndStatus(symbol, status, pageable);
        }
        if (status != null) {
            return alertRepository.findByStatus(status, pageable);
        }
        if (symbol != null) {
            return alertRepository.findBySymbol(symbol, pageable);
        }
        return alertRepository.findAll(pageable);
    }

    public Alert createAlert(Alert alert) {
        alert.setStatus(AlertStatus.ACTIVE);
        alertRepository.save(alert);
        alertCache.addAlert(alert);
        return alert;
    }

    public void createAlert(String symbol, String type, BigDecimal targetPrice, Long userId){
        Alert alert=new Alert();
        alert.setSymbol(symbol);
        alert.setTargetPrice(targetPrice);
        alert.setUserId(userId);
        alert.setConditionType(ConditionType.valueOf(type));
        createAlert(alert);
    }

    public Alert getAlertById(Long id) {
        return alertRepository.findById(id).orElseThrow(() -> new ResourseNotFoundException("Alert not found " + id));
    }


}
