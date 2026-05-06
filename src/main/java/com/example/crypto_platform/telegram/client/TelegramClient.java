package com.example.crypto_platform.telegram.client;

import com.example.crypto_platform.telegram.dto.TelegramResponse;
import com.example.crypto_platform.telegram.dto.TelegramUpdate;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component

public class TelegramClient {
    private final WebClient telegramWebClient;
    public TelegramClient(WebClient telegramWebClient){
        this.telegramWebClient=telegramWebClient;
    }


    public void sendMessage(Long chatId, String text){
        telegramWebClient
                .post()
                .uri("/sendMessage")
                .bodyValue(Map.of("chat_id",chatId,"text",text))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public void sendMessageWithAlertDirectionButtons(Long chatId,String text){
        Map<String,Object> replyMarkup =Map.of(
                "inline_keyboard", List.of(
                        List.of(
                                Map.of("text","Above","callback_data","ALERT_DIRECTION:ABOVE"),
                                Map.of("text","Below","callback_data","ALERT_DIRECTION:BELOW")
                        )
                )
        );
        Map<String,Object> body=Map.of(
                "chat_id",chatId,"text",text,"reply_markup",replyMarkup);
        telegramWebClient
                .post()
                .uri("/sendMessage")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }


    public void sendPhoto(Long chatId, byte[] imageBytes,String fileName){
        ByteArrayResource imageResource =new ByteArrayResource(imageBytes){
          @Override
          public String getFilename(){
              return fileName;
          }
        };

        MultipartBodyBuilder bodyBuilder=new MultipartBodyBuilder();
        bodyBuilder.part("chat_id",chatId);
        bodyBuilder.part("photo", imageResource).filename(fileName).contentType(MediaType.IMAGE_PNG);
        telegramWebClient.post()
                .uri("/sendPhoto")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public List<TelegramUpdate> getUpdates(Long offset){
        TelegramResponse response=telegramWebClient
                .get()
                .uri(uriBuilder -> uriBuilder.path("/getUpdates").queryParam("timeout",30).
                        queryParamIfPresent("offset",offset==null?Optional.empty(): Optional.of(offset)).build())
                .retrieve()
                .bodyToMono(TelegramResponse.class)
                .block();

        if (response == null || !response.ok()) {
            return List.of();
        }
        return response.result();
    }



}
