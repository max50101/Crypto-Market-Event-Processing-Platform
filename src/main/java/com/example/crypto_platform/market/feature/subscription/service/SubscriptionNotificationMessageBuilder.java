package com.example.crypto_platform.market.feature.subscription.service;

import com.example.crypto_platform.common.event.SubscriptionUpdateEvent;
import com.example.crypto_platform.market.feature.chart.ChartService;
import com.example.crypto_platform.market.feature.currentprice.CurrentPriceQuarryService;
import com.example.crypto_platform.market.feature.indicators.service.RsiService;
import com.example.crypto_platform.market.feature.subscription.SubscriptionRepository;
import com.example.crypto_platform.market.feature.subscription.dto.SubscriptionNotificationMessage;
import com.example.crypto_platform.market.feature.subscription.entity.ChartSubscription;
import com.example.crypto_platform.user.UserService;
import com.example.crypto_platform.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class SubscriptionNotificationMessageBuilder {
    private final SubscriptionRepository repository;
    private final ChartService chartBuilder;
    private final UserService userService;
    private final RsiService rsiService;
    private final CurrentPriceQuarryService quarryService;
    public SubscriptionNotificationMessage build(SubscriptionUpdateEvent event){
        ChartSubscription subscription=repository
                .findById(event.subscriptionId())
                .orElseThrow(()->new IllegalStateException("No such subscription"));
        User user=userService.findByUserId(subscription.getUser_id());
        byte[] chart= chartBuilder.generatePriceChart(subscription.getSymbol(),subscription.getInterval());
        BigDecimal rsi=rsiService.calculateRsi(subscription.getSymbol(),subscription.getInterval());
        BigDecimal price=quarryService.getCurrentPrice(subscription.getSymbol());
        String caption = subscription.getSymbol() + " "
                + subscription.getInterval()
                + " chart"
                +"\nprice: "+price+"\nrsi: "+rsi;

        String filename = subscription.getSymbol()
                + "-"
                + subscription.getInterval()
                + ".png";

        return new SubscriptionNotificationMessage(
                subscription.getId(),
                user.getId(),
                user.getTelegramChatId(),
                user.getEmail(),
                subscription.getSymbol(),
                subscription.getInterval(),
                chart,
                filename,
                caption,
                subscription.isSendTelegram(),
                subscription.isSendEmail()
        );
    }
}
