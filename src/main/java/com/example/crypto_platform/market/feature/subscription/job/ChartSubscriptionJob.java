package com.example.crypto_platform.market.feature.subscription.job;

import com.example.crypto_platform.common.event.SubscriptionUpdateEvent;
import com.example.crypto_platform.market.feature.subscription.entity.ChartSubscription;
import com.example.crypto_platform.market.feature.subscription.SubscriptionRepository;
import com.example.crypto_platform.market.feature.subscription.entity.SubscriptionStatus;
import com.example.crypto_platform.market.messaging.SubscriptionProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChartSubscriptionJob {
    private static final int BATCH_SIZE=100;
    private  final SubscriptionRepository repo;
    private final SubscriptionProducer producer;

    @Scheduled(fixedDelay = 60_000)
    public void processDueSubscriptions(){
        Instant now=Instant.now();
        List<ChartSubscription> chartSubscriptionList=
                repo.findByStatusAndNextSendAtLessThanEqualOrderByNextSendAtAsc(SubscriptionStatus.ACTIVE,now, PageRequest.of(0,BATCH_SIZE));
        if(chartSubscriptionList.isEmpty()){
            return;
        }
        for(ChartSubscription subscription: chartSubscriptionList){
            processOne(now,subscription);
        }
    }

    private void processOne(Instant now, ChartSubscription subscription){
        producer.sendSubscriptionUpdate(new SubscriptionUpdateEvent(subscription.getId()));
        subscription.setStatus(SubscriptionStatus.PROCESSING);
        repo.save(subscription);
    }
}

