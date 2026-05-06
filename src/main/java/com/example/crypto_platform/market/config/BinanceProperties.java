package com.example.crypto_platform.market.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ConfigurationProperties(prefix="binance")
public class BinanceProperties {

    List<String> symbols= new ArrayList<>();
    String baseUrl;
    String baseApiUrl;

    public List<String> getSymbols() {
        return symbols;
    }

    public void setSymbols(List<String> symbols) {
        this.symbols = symbols;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getBaseApiUrl() {
        return baseApiUrl;
    }

    public void setBaseApiUrl(String baseApiUrl) {
        this.baseApiUrl = baseApiUrl;
    }

    public String buildCombinedTradeStreamUrl(){
        String streams=symbols.stream()
                .map(String::toLowerCase)
                .map(s -> s+"@trade")
                .collect(Collectors.joining("/"));
        return baseUrl+"/stream?streams="+streams;
    }
}
