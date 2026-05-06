package com.example.crypto_platform.notification.email.resend;

import com.example.crypto_platform.common.event.AlertTriggeredEvent;
import com.example.crypto_platform.notification.email.EmailSender;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import org.springframework.stereotype.Component;

@Component
public class ResendEmailSender implements EmailSender {
    private final ResendProperties properties;
    private final Resend resend;
    public ResendEmailSender(ResendProperties properties){
        this.properties=properties;
        System.out.println("Api key"+properties.apiKey());
        this.resend=new Resend(properties.apiKey());
    }


    @Override
    public void send(AlertTriggeredEvent event) throws ResendException {
        String message = String.format(
                "<p>Alert %s worked, target price %s, current price %s</p>",
                event.getAlertId(),
                event.getTargetPrice(),
                event.getTriggeredPrice()
        );
        CreateEmailOptions params = CreateEmailOptions.builder()
                .from("Acme <onboarding@resend.dev>")
                .to("makswork1108@gmail.com")
                .subject("Alert worked")
                .html(message)
                .build();

        CreateEmailResponse response = resend.emails().send(params);
        System.out.println("Email sent with ID: " + response.getId());
    }

    @Override
    public void sendEmail(String text, String subject,String email) throws ResendException {
        CreateEmailOptions params=CreateEmailOptions.builder()
                .from("Acme <onboarding@resend.dev>")
                .to(email)
                .subject(subject)
                .text(text)
                .build();
        CreateEmailResponse response = resend.emails().send(params);
        System.out.println("Email sent with ID: " + response.getId());
    }
}

