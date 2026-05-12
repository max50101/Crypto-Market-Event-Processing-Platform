package com.example.crypto_platform.telegram.consumer;

import com.example.crypto_platform.telegram.client.TelegramClient;
import com.example.crypto_platform.telegram.dto.TelegramUpdate;
import com.example.crypto_platform.telegram.service.TelegramUpdateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@Slf4j
public class TelegramPollingConsumer {
    private final TelegramClient telegramClient;
    private final TelegramPollingStateService pollingStateService;
    private final TelegramUpdateHandler handler;
    public TelegramPollingConsumer(TelegramClient telegramClient,TelegramPollingStateService pollingStateService,TelegramUpdateHandler handler){
        this.telegramClient=telegramClient;
        this.pollingStateService=pollingStateService;
        this.handler=handler;
    }

    @Scheduled(fixedDelay = 1000)
    public void poll(){
        Long offset= pollingStateService.getCurrentOffset();
        List<TelegramUpdate> updates;
        try{
            updates=telegramClient.getUpdates(offset);
        }catch (Exception e){
            log.error("Failed to fetch Telegram updates", e);
            return;
        }

        if(updates.isEmpty()){
            return;
        }

        for(TelegramUpdate update:updates){
            try {
                handler.handle(update);
                pollingStateService.saveLastProcessedUpdateId(update.updateId());
            }catch (Exception e){
                log.error("Failed to process update {}",update.updateId(),e);
            }
        }

    }
}
