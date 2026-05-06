package com.example.crypto_platform.notification.email.service;

import com.example.crypto_platform.common.event.AlertTriggeredEvent;
import com.example.crypto_platform.notification.email.EmailSender;
import com.example.crypto_platform.user.UserService;
import com.example.crypto_platform.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailAlertNotificationSender {
    private final UserService userService;
    private final EmailSender emailSender;
    private final EmailAlertMessageBuilder messageBuilder;

    public void send(AlertTriggeredEvent event){
        User user=userService.findByUserId(event.getUserId());
        if(user.getEmail()==null||!user.isEmailVerified()){
            return;
        }
        String body= messageBuilder.build(event);
        String subject= messageBuilder.subject();
        try {
            emailSender.sendEmail(body, subject, user.getEmail());
        } catch (Exception e) {
            log.error("Error in sending alert triggered email",e);
        }

    }
}
