package com.example.crypto_platform.common.handler;

import com.example.crypto_platform.common.exception.EmailVerificationException;
import com.example.crypto_platform.common.exception.ResourseNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourseNotFoundException.class)
    public ResponseEntity<String> handleNotFound(ResourseNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(EmailVerificationException.class)
    public ResponseEntity<String> handleNotFound(EmailVerificationException ex){
        return ResponseEntity.badRequest().body("Ссылка устарела или не найдена");
    }
}
