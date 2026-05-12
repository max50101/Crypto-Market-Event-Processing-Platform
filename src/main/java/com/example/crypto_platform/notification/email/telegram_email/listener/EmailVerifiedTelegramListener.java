package com.example.crypto_platform.notification.email.telegram_email.listener;

import com.example.crypto_platform.notification.email.telegram_email.event.EmailVerifiedEvent;
import com.example.crypto_platform.notification.telegram.TelegramNotificationSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class EmailVerifiedTelegramListener {

    private final TelegramNotificationSender telegramNotificationSender;


    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(EmailVerifiedEvent event){
        telegramNotificationSender.sendEmailNotification(event.chatId());
    }
}
