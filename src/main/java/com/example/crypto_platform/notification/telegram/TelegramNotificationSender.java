package com.example.crypto_platform.notification.telegram;

import com.example.crypto_platform.common.event.AlertTriggeredEvent;
import com.example.crypto_platform.telegram.client.TelegramClient;
import com.example.crypto_platform.user.UserService;
import com.example.crypto_platform.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramNotificationSender {
    private final TelegramClient telegramClient;
    private final UserService userService;
    public void send(AlertTriggeredEvent event){
        User user=userService.findByUserId(event.getUserId());
        telegramClient.sendMessage(user.getTelegramChatId(),"AlertTriggered successfully");
    }
    public void sendEmailNotification(Long chatId){
        telegramClient.sendMessage(chatId,"Email verified successfully");
    }
}
