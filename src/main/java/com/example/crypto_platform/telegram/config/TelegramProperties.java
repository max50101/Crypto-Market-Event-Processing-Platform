package com.example.crypto_platform.telegram.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "telegram.bot")
public class TelegramProperties {
    private String token;
    private String baseUrl;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String buildBotBaseUrl(){
        return baseUrl+"/bot"+token;
    }

    public String buildMethodUrl(String method){
        return buildBotBaseUrl()+"/"+method;
    }
}

