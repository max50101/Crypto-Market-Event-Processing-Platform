package com.example.crypto_platform.alert.cache;

import com.example.crypto_platform.alert.entity.Alert;
import com.example.crypto_platform.alert.entity.AlertStatus;
import com.example.crypto_platform.alert.repository.AlertRepository;
import com.example.crypto_platform.alert.service.AlertService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class AlertCache {
   private ConcurrentHashMap<String, CopyOnWriteArrayList<Alert>>  alertCache=new ConcurrentHashMap<>();
   private final ConcurrentHashMap<Long, Alert> alertById=new ConcurrentHashMap<>();
   private final AlertRepository repository;

   public AlertCache(AlertRepository repository){
       this.repository=repository;
   }

   public List<Alert> getAlertsBySymbol(String symbol){
       return alertCache.getOrDefault(symbol,new CopyOnWriteArrayList<>());
   }

   @PostConstruct
   public void initializeAlertCache(){
       List<Alert> alertList=repository.findByStatus(AlertStatus.ACTIVE);
       for(Alert alert : alertList){
           alertCache.computeIfAbsent(alert.getSymbol(), k-> new CopyOnWriteArrayList<>()).add(alert);
           alertById.putIfAbsent(alert.getId(),alert);
       }
   }

   public void addAlert(Alert alert){
       alertCache.computeIfAbsent(alert.getSymbol(),k->new CopyOnWriteArrayList<>()).add(alert);
       alertById.put(alert.getId(), alert);
   }

   public void removeAlert(Long alertId){
        Alert alert=alertById.remove(alertId);
        if(alert==null){
            return;
        }
        List<Alert> list=alertCache.get(alert.getSymbol());
        if(list==null){
            return;
        }
        list.removeIf(e->e.getId().equals(alertId));
        if(list.isEmpty()){
            alertCache.remove(alert.getSymbol());
        }

   }

   public void updateAlert(Alert alert){
       removeAlert(alert.getId());
       if(alert.getStatus()==AlertStatus.ACTIVE){
           addAlert(alert);
       }
   }
}
