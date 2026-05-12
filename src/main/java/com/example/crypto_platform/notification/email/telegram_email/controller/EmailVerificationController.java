package com.example.crypto_platform.notification.email.telegram_email.controller;

import com.example.crypto_platform.notification.email.telegram_email.sevice.EmailVerificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EmailVerificationController {
    private final EmailVerificationService emailVerificationService;
    @RequestMapping("/verify")
    public ResponseEntity<String> verify(@RequestParam String token){
        emailVerificationService.confirm(token);
        return ResponseEntity.ok("Email verified successfully");
    }
}
