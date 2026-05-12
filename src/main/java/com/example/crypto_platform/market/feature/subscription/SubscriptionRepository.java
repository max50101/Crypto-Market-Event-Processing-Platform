package com.example.crypto_platform.market.feature.subscription;

import com.example.crypto_platform.market.feature.subscription.entity.ChartSubscription;
import com.example.crypto_platform.market.feature.subscription.entity.SubscriptionStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.Instant;
import java.util.List;

public interface SubscriptionRepository  extends JpaRepository<ChartSubscription,Long> {
    List<ChartSubscription> findByStatusAndNextSendAtLessThanEqualOrderByNextSendAtAsc(
            SubscriptionStatus status,
            Instant now,
            Pageable pageable
    );

    boolean existsByUserIdAndSymbolAndIntervalAndFrequencyMinutesAndStatus(
            Long userId,
            String symbol,
            String interval,
            Integer frequencyMinutes,
            SubscriptionStatus status
    );

    List<ChartSubscription>  findByUserIdAndStatusOrderByCreatedAtDesc(
            Long userId,
            SubscriptionStatus status
    );
}
