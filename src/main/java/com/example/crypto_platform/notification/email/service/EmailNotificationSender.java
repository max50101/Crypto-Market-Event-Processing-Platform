package com.example.crypto_platform.notification.email.service;

import com.example.crypto_platform.common.event.AlertTriggeredEvent;
import com.example.crypto_platform.market.feature.subscription.dto.SubscriptionNotificationMessage;
import com.example.crypto_platform.notification.email.EmailSender;
import com.example.crypto_platform.user.UserService;
import com.example.crypto_platform.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailNotificationSender {
    private final UserService userService;
    private final EmailSender emailSender;
    private final EmailMessageBuilder messageBuilder;

    public void send(AlertTriggeredEvent event){
        User user=userService.findByUserId(event.getUserId());
        if(user.getEmail()==null||!user.isEmailVerified()){
            return;
        }
        String body= messageBuilder.build(event);
        String subject= messageBuilder.subjectAlert();
        try {
            emailSender.sendEmail(body, subject, user.getEmail());
        } catch (Exception e) {
            log.error("Error in sending alert triggered email",e);
        }
    }

    public void send(SubscriptionNotificationMessage message){
        User user=userService.findByUserId(message.userId());
        if(user.getEmail()==null||!user.isEmailVerified()){
            return;
        }
        String subject=messageBuilder.subjectSubscription();
        String body=message.caption();
        try {
                emailSender.sendEmailWithAttachment(body,subject,message.chartImage(), user.getEmail());
        }catch (Exception e){
            log.error("Error in sending chart image");
        }

    }
}
