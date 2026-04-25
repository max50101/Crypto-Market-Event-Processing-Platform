package com.example.crypto_platform.notification.email;

import com.example.crypto_platform.common.event.AlertTriggeredEvent;
import com.example.crypto_platform.notification.email.resend.EmailSender;
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

    public void send(AlertTriggeredEvent event){
        User user=userService.findByUserId(event.getUserId());

    }
}
