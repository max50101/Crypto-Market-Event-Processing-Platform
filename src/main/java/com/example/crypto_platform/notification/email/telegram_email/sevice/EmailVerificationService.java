package com.example.crypto_platform.notification.email.telegram_email;

import com.example.crypto_platform.common.exception.EmailVerificationException;
import com.example.crypto_platform.common.exception.ResourseNotFoundException;
import com.example.crypto_platform.common.properties.AppProperties;
import com.example.crypto_platform.notification.email.EmailSender;
import com.example.crypto_platform.user.UserService;
import com.example.crypto_platform.user.entity.User;
import com.resend.core.exception.ResendException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailVerificationService {
    private final UserService userService;
    private final EmailSender sender;
    private final EmailVerificationTokenRepository tokenRepository;
    private final AppProperties appProperties;

    @Transactional
    public void startVerification(User user, String email){
        user.setEmail(email);
        user.setEmailVerified(false);
        userService.save(user);
        EmailVerificationToken emailVerificationToken=new EmailVerificationToken();
        emailVerificationToken.setUserId(user.getId());
        emailVerificationToken.setToken(UUID.randomUUID().toString());
        emailVerificationToken.setExpiresAt(Instant.now().plus(Duration.ofHours(24)));
        tokenRepository.save(emailVerificationToken);
        String verifyLink = appProperties.getBaseUrl()
                + "/api/email/verify?token="
                + emailVerificationToken.getToken();

        String subject = "Confirm your email";
        String body = """
                Подтверди email, перейдя по ссылке:
                                
                %s
                """.formatted(verifyLink);
        try {
            sender.sendEmail(body, subject, email);
        }catch (ResendException e ){
            log.error("Failed to send email for verification");
        }
    }

    @Transactional
    public void confirm(String rawToken){
        EmailVerificationToken token=tokenRepository.findByToken(rawToken)
                .orElseThrow(()->new ResourseNotFoundException("Token not found"));
        if(token.isUsed()){
            throw  new EmailVerificationException("Token is used");
        }
        if(token.getExpiresAt().isBefore(Instant.now())){
            throw  new EmailVerificationException("Token is expired");
        }
        User user=userService.findByUserId(token.getUserId());
        user.setEmailVerified(true);
        userService.save(user);
        token.setUsed(true);
        tokenRepository.save(token);

    }
}
