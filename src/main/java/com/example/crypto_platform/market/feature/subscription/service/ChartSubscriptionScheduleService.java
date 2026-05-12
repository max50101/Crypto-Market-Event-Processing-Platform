package com.example.crypto_platform.market.feature.subscription.service;

import com.example.crypto_platform.market.feature.subscription.SubscriptionRepository;
import com.example.crypto_platform.market.feature.subscription.entity.ChartSubscription;
import com.example.crypto_platform.market.feature.subscription.entity.SubscriptionStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
@Service
public class ChartSubscriptionScheduleService {
    private final SubscriptionRepository repo;
    @Transactional
    public void markSent(Long subscriptionId){
        ChartSubscription subscription=repo.findById(subscriptionId).orElseThrow(()->new IllegalStateException("Subscription not found"));
        Instant now= Instant.now();
        subscription.setLastSentAt(now);
        subscription.setNextSendAt(subscription.getNextSendAt().plus(subscription.getFrequencyMinutes(), ChronoUnit.MINUTES));
        subscription.setStatus(SubscriptionStatus.ACTIVE);
    }

    @Transactional
    public void markFailed(Long subscriptionId){
        ChartSubscription subscription=repo.findById(subscriptionId).orElseThrow(()->new IllegalStateException("Subscription not found"));
        Instant now= Instant.now();
        subscription.setNextSendAt(subscription.getNextSendAt().plus(5,ChronoUnit.MINUTES));
        subscription.setStatus(SubscriptionStatus.ACTIVE);
    }



}
