package com.example.crypto_platform.notification.email.resend;

import com.example.crypto_platform.common.event.AlertTriggeredEvent;
import com.example.crypto_platform.notification.email.EmailSender;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.Attachment;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.List;

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
    public void sendEmail(String text, String subject,String toEmail) throws ResendException {
        CreateEmailOptions params=CreateEmailOptions.builder()
                .from("Acme <onboarding@resend.dev>")
                .to(toEmail)
                .subject(subject)
                .text(text)
                .build();
        CreateEmailResponse response = resend.emails().send(params);
        System.out.println("Email sent with ID: " + response.getId());
    }

    @Override
    public void sendEmailWithAttachment(String text, String subject,
                                        byte[] imageBytes, String toEmail) throws ResendException{
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
        Attachment photo = Attachment.builder()
                .fileName("chart.png")
                .content(base64Image)
                .contentType("image/png")
                .contentId("chart-image")
                .build();
        CreateEmailOptions params=CreateEmailOptions.builder()
                .from("Acme <onboarding@resend.dev>")
                .to(toEmail)
                .subject(subject)
                .text(text)
                .attachments(List.of(photo))
                .build();
        CreateEmailResponse response = resend.emails().send(params);
        System.out.println("Email sent with ID: " + response.getId());

    }
}

