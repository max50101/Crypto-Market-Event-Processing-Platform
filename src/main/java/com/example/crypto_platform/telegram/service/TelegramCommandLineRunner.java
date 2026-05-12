package com.example.crypto_platform.telegram.service;

import com.example.crypto_platform.alert.service.AlertService;
import com.example.crypto_platform.market.feature.chart.ChartService;
import com.example.crypto_platform.market.feature.currentprice.CurrentPriceQuarryService;
import com.example.crypto_platform.market.feature.indicators.service.RsiService;

import com.example.crypto_platform.market.feature.subscription.entity.ChartSubscription;
import com.example.crypto_platform.market.feature.subscription.service.ChartSubscriptionService;
import com.example.crypto_platform.notification.email.telegram_email.sevice.EmailVerificationService;
import com.example.crypto_platform.telegram.client.TelegramClient;
import com.example.crypto_platform.telegram.conversation.TelegramConversationService;
import com.example.crypto_platform.telegram.conversation.TelegramConversationState;
import com.example.crypto_platform.telegram.conversation.TelegramScenario;
import com.example.crypto_platform.telegram.conversation.TelegramStep;
import com.example.crypto_platform.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TelegramCommandLineRunner {

    private static final String ALERT_DIRECTION_PREFIX = "ALERT_DIRECTION:";
    private static final String ALERT_DIRECTION_ABOVE = "ABOVE";
    private static final String ALERT_DIRECTION_BELOW = "BELOW";

    private final AlertService alertService;
    private final TelegramClient client;
    private final EmailVerificationService emailVerificationService;
    private final CurrentPriceQuarryService currentPriceQuarryService;
    private final TelegramConversationService conversationService;
    private final RsiService rsiService;
    private final ChartService chartService;
    private final ChartSubscriptionService marketSubscriptionService;

    public void run(User user, String text) {
        if (text == null || text.isBlank()) {
            client.sendMessage(user.getTelegramChatId(), "Empty message. Use /help");
            return;
        }

        String command = text.trim();

        if (command.equals("/start")) {
            handleStart(user);
            return;
        }

        if (command.equals("/help")) {
            handleHelp(user);
            return;
        }

        if (command.equals("/cancel")) {
            cancelScenario(user);
            return;
        }

        if (command.startsWith("/price")) {
            handlePrice(user, command);
            return;
        }

        if (command.startsWith("/email")) {
            handleEmail(user, command);
            return;
        }

        if (command.startsWith("/rsi")) {
            handleRsi(user, command);
            return;
        }

        if (command.startsWith("/chart")) {
            handlePriceChart(user, command);
            return;
        }

        if (command.startsWith("/subscribe")) {
            handleSubscribe(user, command);
            return;
        }

        if (command.equals("/subscriptions")) {
            handleSubscriptions(user);
            return;
        }

        if (command.startsWith("/cancel_subscribe")) {
            handleCancelSubscribe(user, command);
            return;
        }

        if (command.equals("/create_alert")) {
            startCreateAlertScenario(user);
            return;
        }

        Optional<TelegramConversationState> stateOptional =
                conversationService.get(user.getTelegramChatId());

        if (stateOptional.isPresent()) {
            handleScenarioMessage(user, command, stateOptional.get());
            return;
        }

        client.sendMessage(
                user.getTelegramChatId(),
                "Unknown command. Use /help"
        );
    }

    public void runCallback(User user, String callbackData) {
        if (callbackData == null || callbackData.isBlank()) {
            client.sendMessage(
                    user.getTelegramChatId(),
                    "Unknown button action"
            );
            return;
        }

        Optional<TelegramConversationState> stateOptional =
                conversationService.get(user.getTelegramChatId());

        if (stateOptional.isEmpty()) {
            client.sendMessage(
                    user.getTelegramChatId(),
                    "No active scenario. Use /create_alert to start."
            );
            return;
        }

        TelegramConversationState state = stateOptional.get();

        if (state.scenario() == TelegramScenario.CREATE_ALERT) {
            handleCreateAlertCallback(user, callbackData, state);
            return;
        }

        conversationService.delete(user.getTelegramChatId());

        client.sendMessage(
                user.getTelegramChatId(),
                "Unknown scenario. State cleared."
        );
    }

    private void handleScenarioMessage(
            User user,
            String text,
            TelegramConversationState state
    ) {
        if (state.scenario() == TelegramScenario.CREATE_ALERT) {
            handleCreateAlertMessage(user, text, state);
            return;
        }

        conversationService.delete(user.getTelegramChatId());

        client.sendMessage(
                user.getTelegramChatId(),
                "Unknown scenario. State cleared."
        );
    }

    private void startCreateAlertScenario(User user) {
        conversationService.delete(user.getTelegramChatId());

        TelegramConversationState state = new TelegramConversationState(
                TelegramScenario.CREATE_ALERT,
                TelegramStep.WAITING_SYMBOL,
                null,
                null,
                null,
                null
        );

        conversationService.save(user.getTelegramChatId(), state);

        client.sendMessage(
                user.getTelegramChatId(),
                """
                Creating alert.

                Enter symbol:
                Example: BTCUSDT

                To cancel: /cancel
                """
        );
    }

    private void handleCreateAlertMessage(
            User user,
            String text,
            TelegramConversationState state
    ) {
        switch (state.step()) {
            case WAITING_SYMBOL -> handleWaitingSymbol(user, text);

            case WAITING_DIRECTION -> client.sendMessageWithAlertDirectionButtons(
                    user.getTelegramChatId(),
                    "Please choose direction using buttons:"
            );

            case WAITING_PRICE -> handleWaitingPrice(user, text, state);

            default -> {
                conversationService.delete(user.getTelegramChatId());

                client.sendMessage(
                        user.getTelegramChatId(),
                        "Scenario was reset. Use /create_alert again."
                );
            }
        }
    }

    private void handleWaitingSymbol(User user, String text) {
        String symbol = text.trim().toUpperCase();

        if (symbol.isBlank()) {
            client.sendMessage(
                    user.getTelegramChatId(),
                    "Symbol cannot be empty. Example: BTCUSDT"
            );
            return;
        }

        TelegramConversationState newState = new TelegramConversationState(
                TelegramScenario.CREATE_ALERT,
                TelegramStep.WAITING_DIRECTION,
                symbol,
                null,
                null,
                null
        );

        conversationService.save(user.getTelegramChatId(), newState);

        client.sendMessageWithAlertDirectionButtons(
                user.getTelegramChatId(),
                "Choose alert direction for " + symbol + ":"
        );
    }

    private void handleCreateAlertCallback(
            User user,
            String callbackData,
            TelegramConversationState state
    ) {
        if (state.step() != TelegramStep.WAITING_DIRECTION) {
            client.sendMessage(
                    user.getTelegramChatId(),
                    "This button is not expected now."
            );
            return;
        }

        if (!callbackData.startsWith(ALERT_DIRECTION_PREFIX)) {
            client.sendMessage(
                    user.getTelegramChatId(),
                    "Unknown alert direction."
            );
            return;
        }

        String direction = callbackData.substring(ALERT_DIRECTION_PREFIX.length())
                .trim()
                .toUpperCase();

        if (!direction.equals(ALERT_DIRECTION_ABOVE)
                && !direction.equals(ALERT_DIRECTION_BELOW)) {
            client.sendMessage(
                    user.getTelegramChatId(),
                    "Direction must be ABOVE or BELOW"
            );
            return;
        }

        TelegramConversationState newState = new TelegramConversationState(
                TelegramScenario.CREATE_ALERT,
                TelegramStep.WAITING_PRICE,
                state.symbol(),
                null,
                direction,
                null
        );

        conversationService.save(user.getTelegramChatId(), newState);

        client.sendMessage(
                user.getTelegramChatId(),
                """
                Enter target price.
                Example: 65000
                """
        );
    }

    private void handleWaitingPrice(
            User user,
            String text,
            TelegramConversationState state
    ) {
        BigDecimal targetPrice;

        try {
            targetPrice = new BigDecimal(text.trim());
        } catch (NumberFormatException e) {
            client.sendMessage(
                    user.getTelegramChatId(),
                    "Target price must be a number. Example: 65000 or 65000.50"
            );
            return;
        }

        if (targetPrice.compareTo(BigDecimal.ZERO) <= 0) {
            client.sendMessage(
                    user.getTelegramChatId(),
                    "Target price must be greater than 0"
            );
            return;
        }

        alertService.createAlert(
                state.symbol(),
                state.direction(),
                targetPrice,
                user.getId()
        );

        conversationService.delete(user.getTelegramChatId());

        client.sendMessage(
                user.getTelegramChatId(),
                "Alert created: " +
                        state.symbol() + " " +
                        state.direction() + " " +
                        targetPrice
        );
    }

    private void handlePrice(User user, String text) {
        String[] parts = text.split("\\s+");

        if (parts.length < 2) {
            client.sendMessage(
                    user.getTelegramChatId(),
                    """
                    Usage:
                    /price BTCUSDT
                    """
            );
            return;
        }

        String symbol = parts[1].trim().toUpperCase();

        try {
            BigDecimal price = currentPriceQuarryService.getCurrentPrice(symbol);

            client.sendMessage(
                    user.getTelegramChatId(),
                    symbol + " price: " + price
            );
        } catch (Exception e) {
            client.sendMessage(
                    user.getTelegramChatId(),
                    "Could not get price for " + symbol
            );
        }
    }

    private void handleRsi(User user, String text) {
        String[] parts = text.split("\\s+");

        if (parts.length < 2) {
            client.sendMessage(
                    user.getTelegramChatId(),
                    """
                    Usage:
                    /rsi BTCUSDT
                    /rsi BTCUSDT 15m
                    """
            );
            return;
        }

        String symbol = parts[1].trim().toUpperCase();

        try {
            String interval = parts.length > 2 ? parts[2].trim() : "15m";

            BigDecimal rsi = parts.length > 2
                    ? rsiService.calculateRsi(symbol, interval)
                    : rsiService.calculateRsi(symbol);

            client.sendMessage(
                    user.getTelegramChatId(),
                    symbol + " RSI " + interval + ": " + rsi
            );
        } catch (Exception e) {
            client.sendMessage(
                    user.getTelegramChatId(),
                    "Could not get RSI for " + symbol
            );
        }
    }

    private void handleEmail(User user, String text) {
        String[] parts = text.split("\\s+");

        if (parts.length < 2) {
            client.sendMessage(
                    user.getTelegramChatId(),
                    """
                    Usage:
                    /email your_email@example.com
                    """
            );
            return;
        }

        String email = parts[1].trim().toLowerCase();

        emailVerificationService.startVerification(user, email);

        client.sendMessage(
                user.getTelegramChatId(),
                "Follow link in email"
        );
    }

    private void handlePriceChart(User user, String text) {
        String[] parts = text.split("\\s+");

        if (parts.length < 2) {
            client.sendMessage(
                    user.getTelegramChatId(),
                    """
                    Usage:
                    /chart BTCUSDT
                    /chart BTCUSDT 15m
                    """
            );
            return;
        }

        String symbol = parts[1].trim().toUpperCase();
        String interval = parts.length > 2 ? parts[2].trim() : "15m";

        try {
            byte[] chart = chartService.generatePriceChart(symbol, interval);

            client.sendPhoto(
                    user.getTelegramChatId(),
                    chart,
                    "PriceChart"
            );
        } catch (Exception e) {
            client.sendMessage(
                    user.getTelegramChatId(),
                    "Could not generate chart for " + symbol
            );
        }
    }

    private void handleSubscribe(User user, String text) {
        String[] parts = text.split("\\s+");

        if (parts.length < 4) {
            client.sendMessage(
                    user.getTelegramChatId(),
                    """
                    Usage:
                    /subscribe BTCUSDT 15m 1h

                    Examples:
                    /subscribe BTCUSDT 15m 1h
                    /subscribe ETHUSDT 5m 5m

                    Format:
                    /subscribe SYMBOL CHART_INTERVAL SEND_FREQUENCY
                    """
            );
            return;
        }

        String symbol = parts[1].trim().toUpperCase();
        String interval = parts[2].trim();
        String frequency = parts[3].trim();

        try {
            ChartSubscription subscription = marketSubscriptionService.subscribe(
                    user,
                    symbol,
                    interval,
                    frequency,
                    true
            );

            client.sendMessage(
                    user.getTelegramChatId(),
                    "Subscription created ✅\n\n" +
                            "#" + subscription.getId() + "\n" +
                            "Symbol: " + subscription.getSymbol() + "\n" +
                            "Chart interval: " + subscription.getInterval() + "\n" +
                            "Send every: " + subscription.getFrequencyMinutes() + " minutes"
            );

        } catch (Exception e) {
            client.sendMessage(
                    user.getTelegramChatId(),
                    "Could not create subscription: " + e.getMessage()
            );
        }
    }

    private void handleSubscriptions(User user) {
        try {
            List<ChartSubscription> subscriptions =
                    marketSubscriptionService.getSubscription(user);

            if (subscriptions.isEmpty()) {
                client.sendMessage(
                        user.getTelegramChatId(),
                        "You have no active subscriptions."
                );
                return;
            }

            StringBuilder message = new StringBuilder("Your active subscriptions:\n\n");

            for (ChartSubscription subscription : subscriptions) {
                message.append("#")
                        .append(subscription.getId())
                        .append(" ")
                        .append(subscription.getSymbol())
                        .append(" ")
                        .append(subscription.getInterval())
                        .append(" every ")
                        .append(subscription.getFrequencyMinutes())
                        .append("m")
                        .append("\n");
            }

            message.append("\nTo cancel subscription:\n");
            message.append("/cancel_subscribe <id>");

            client.sendMessage(
                    user.getTelegramChatId(),
                    message.toString()
            );

        } catch (Exception e) {
            client.sendMessage(
                    user.getTelegramChatId(),
                    "Could not get subscriptions."
            );
        }
    }

    private void handleCancelSubscribe(User user, String text) {
        String[] parts = text.split("\\s+");

        if (parts.length < 2) {
            client.sendMessage(
                    user.getTelegramChatId(),
                    """
                    Usage:
                    /cancel_subscribe 12
                    """
            );
            return;
        }

        Long subscriptionId;

        try {
            subscriptionId = Long.valueOf(parts[1]);
        } catch (NumberFormatException e) {
            client.sendMessage(
                    user.getTelegramChatId(),
                    "Subscription id must be a number."
            );
            return;
        }

        try {
            marketSubscriptionService.cancelSubscription(user, subscriptionId);

            client.sendMessage(
                    user.getTelegramChatId(),
                    "Subscription cancelled ✅ #" + subscriptionId
            );

        } catch (Exception e) {
            client.sendMessage(
                    user.getTelegramChatId(),
                    "Could not cancel subscription: " + e.getMessage()
            );
        }
    }

    private void handleStart(User user) {
        client.sendMessage(
                user.getTelegramChatId(),
                """
                Nihao user 👋

                Available commands:

                /price BTCUSDT
                /rsi BTCUSDT 15m
                /chart BTCUSDT 15m
                /subscribe BTCUSDT 15m 1h
                /subscriptions
                /cancel_subscribe 12
                /create_alert
                /email your_email@example.com
                /cancel
                /help
                """
        );
    }

    private void handleHelp(User user) {
        client.sendMessage(
                user.getTelegramChatId(),
                """
                Available commands:

                /price BTCUSDT
                Get current price.

                /rsi BTCUSDT 15m
                Get RSI by interval. Default interval is 15m.

                /chart BTCUSDT 15m
                Get price chart.

                /subscribe BTCUSDT 15m 1h
                Subscribe to chart delivery.
                Example: send BTCUSDT 15m chart every 1 hour.

                /subscriptions
                Show your active subscriptions.

                /cancel_subscribe 12
                Cancel subscription by id.

                /create_alert
                Create price alert step by step.

                /email your_email@example.com
                Verify email.

                /cancel
                Cancel current scenario.
                """
        );
    }

    private void cancelScenario(User user) {
        conversationService.delete(user.getTelegramChatId());

        client.sendMessage(
                user.getTelegramChatId(),
                "Current scenario cancelled"
        );
    }
}