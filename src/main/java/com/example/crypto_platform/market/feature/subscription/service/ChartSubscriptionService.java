package com.example.crypto_platform.market.feature.subscription.service;

import com.example.crypto_platform.market.feature.common.MarketInterval;
import com.example.crypto_platform.market.feature.subscription.entity.ChartSubscription;
import com.example.crypto_platform.market.feature.subscription.SubscriptionRepository;
import com.example.crypto_platform.market.feature.subscription.entity.SubscriptionStatus;
import com.example.crypto_platform.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChartSubscriptionService {
    private final SubscriptionRepository repo;
    public ChartSubscription subscribe(
            User user,
            String symbol,
            String interval,
            String frequency,
            boolean sendEmail
    ){
        String normalizedSymbol = normalizeSymbol(symbol);
        String normalizedInterval = MarketInterval.fromValue(interval);
        int frequencyMinutes = normalizeFrequency(frequency);

        boolean exists=repo.existsByUserIdAndSymbolAndIntervalAndFrequencyMinutesAndStatus(
                user.getId(),
                normalizedSymbol,
                normalizedInterval,
                frequencyMinutes,
                SubscriptionStatus.ACTIVE
        );
        if(exists){
            return getSubscription(user).stream().findFirst().orElseGet(ChartSubscription::new);
        }
        Instant now=Instant.now();
        ChartSubscription chartSubscription=new ChartSubscription();
        chartSubscription.setStatus(SubscriptionStatus.ACTIVE);
        chartSubscription.setUser_id(user.getId());
        chartSubscription.setSymbol(normalizedSymbol);
        chartSubscription.setInterval(normalizedInterval);
        chartSubscription.setFrequencyMinutes(frequencyMinutes);
        chartSubscription.setSendEmail(sendEmail);
        chartSubscription.setSendTelegram(true);
        chartSubscription.setCreatedAt(now);
        chartSubscription.setNextSendAt(now.plus(0, ChronoUnit.MINUTES));
        return repo.save(chartSubscription);
    }

    public List<ChartSubscription> getSubscription(User user){
        return repo.findByUserIdAndStatusOrderByCreatedAtDesc(user.getId(),SubscriptionStatus.ACTIVE);
    }



    public void cancelSubscription(User user, Long subscriptionId){
        ChartSubscription chartSubscription=repo.findById(subscriptionId).orElseThrow(()->new IllegalArgumentException("Subscription not found"));
        if (!chartSubscription.getUser_id().equals(user.getId())) {
            throw new IllegalArgumentException("Subscription does not belong to user");
        }
        chartSubscription.setStatus(SubscriptionStatus.CANCELED);
        repo.save(chartSubscription);
    }

    private String normalizeSymbol(String symbol){
        if(symbol==null||symbol.isBlank()){
            throw  new IllegalArgumentException("Incorrect symbol");
        }
        return symbol.trim().toUpperCase();
    }

    private int normalizeFrequency(String frequency){
        if(frequency==null||frequency.isBlank()){
            throw  new IllegalArgumentException("Incorrect frequency");
        }
        String normalized=frequency.trim().toLowerCase();
        return switch (normalized){
            case  "15m" -> 15;
            case "30m" -> 30;
            case "1h" -> 60;
            case "4h" -> 240;
            case "1d" -> 1440;
            default -> throw new IllegalArgumentException("Unsupported frequency: " + frequency);
        };
    }
}
