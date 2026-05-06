package com.example.crypto_platform.telegram.service;

import com.example.crypto_platform.alert.service.AlertService;
import com.example.crypto_platform.market.feature.chart.ChartService;
import com.example.crypto_platform.market.feature.currentprice.CurrentPriceQuarryService;
import com.example.crypto_platform.market.feature.indicators.service.RsiService;
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

    /**
     * Обычные текстовые сообщения:
     * /start
     * /help
     * /price BTCUSDT
     * /email test@gmail.com
     * /create_alert
     * BTCUSDT
     * 65000
     */
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

        if(command.startsWith("/rsi")) {
            handleRsi(user,command);
            return;
        }

        if (command.equals("/create_alert")) {
            startCreateAlertScenario(user);
            return;
        }
        if(command.startsWith("/chart")){
            handlePriceChart(user,command);
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

    /**
     * Callback query от inline-кнопок.
     *
     * Например:
     * callbackData = ALERT_DIRECTION:ABOVE
     */
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
                    /price BTCUSDT
                    """
            );
            return;
        }

        String symbol = parts[1].trim().toUpperCase();


        try {
            BigDecimal rsi =
                    parts.length>2? rsiService.calculateRsi(symbol,parts[2].trim()) :rsiService.calculateRsi(symbol);

            client.sendMessage(
                    user.getTelegramChatId(),
                    symbol + " rsi: " + rsi
            );
        } catch (Exception e) {
            client.sendMessage(
                    user.getTelegramChatId(),
                    "Could not get rsi for " + symbol
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

    private void handlePriceChart(User user, String text){
        String[] parts = text.split("\\s+");
        if(parts.length<2){
            client.sendMessage(
                    user.getTelegramChatId(),
                    """
                    Usage:
                    /chart SYMBOL
                    """);
        }
        String symbol=parts[1].trim().toUpperCase();
        client.sendPhoto(user.getTelegramChatId(),
                parts.length>2?chartService.generatePriceChart(symbol,parts[2].trim())
                        :chartService.generatePriceChart(symbol,"15m"),"PriceChart");


    }

    private void handleStart(User user) {
        client.sendMessage(
                user.getTelegramChatId(),
                """
                Nihao user 👋

                Available commands:

                /price BTCUSDT
                /rise SYMBOL
                /create_alert
                /email your_email@example.com
                /chart SYMBOL interval
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

                /create_alert
                Create alert step by step.

                /email your_email@example.com
                Verify email.
                
                /rsi symbol (interval)
                GET RSI by interval (15m default)
                
                /chart SYMBOL interval

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