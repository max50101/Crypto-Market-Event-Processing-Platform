package com.example.crypto_platform.telegram.service;

import com.example.crypto_platform.telegram.dto.TelegramCallbackQuarry;
import com.example.crypto_platform.telegram.dto.TelegramMessage;
import com.example.crypto_platform.telegram.dto.TelegramUpdate;
import com.example.crypto_platform.user.UserService;
import com.example.crypto_platform.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TelegramUpdateHandler {
    private final UserService service;
    private final TelegramCommandLineRunner runner;

    public void handle(TelegramUpdate update){
        if(update!=null&&update.message()!=null) {
            TelegramMessage message = update.message();
            if (message.text() == null || message.from() == null || message.chat() == null) {
                return;
            }
            Long telegramUserId = message.from().id();
            Long telegramChatId = message.chat().id();
            String telegramUserName = message.from().username();
            String text = message.text();
            User user = service.findOrCreateTelegramUser(telegramUserId, telegramChatId, telegramUserName);
            runner.run(user, text);
            return;
        }
        if(update.callbackQuarry()!=null){
            TelegramCallbackQuarry callbackQuarry=update.callbackQuarry();
            if(callbackQuarry.data()==null){
                return;
            }
            Long telegramUserId = callbackQuarry.from().id();
            Long telegramChatId = callbackQuarry.message().chat().id();
            String telegramUserName = callbackQuarry.message().from().username();
            User user = service.findOrCreateTelegramUser(telegramUserId, telegramChatId, telegramUserName);
            String callbackData=callbackQuarry.data();
            runner.runCallback(user,callbackData);
        }
    }



}
