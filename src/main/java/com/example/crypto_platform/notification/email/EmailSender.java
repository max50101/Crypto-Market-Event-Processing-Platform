package com.example.crypto_platform.notification.email;

import com.example.crypto_platform.common.event.AlertTriggeredEvent;
import com.resend.core.exception.ResendException;

public interface EmailSender {
    void send(AlertTriggeredEvent event) throws ResendException;
    void sendEmail(String text, String subject,String email) throws ResendException;

    void sendEmailWithAttachment(String text, String subject,
                                 byte[] imageBytes, String toEmail) throws ResendException;
}
