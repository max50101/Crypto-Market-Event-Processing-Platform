package com.example.crypto_platform.market.client;

import com.example.crypto_platform.market.config.BinanceProperties;
import com.example.crypto_platform.market.messaging.MarketMessageHandler;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.net.URI;
import java.time.Duration;

@Component
@Slf4j
public class BinanceWebSocketClient {
    private final BinanceProperties binanceProperties;
    private final MarketMessageHandler marketMessageHandler;
    private final ReactorNettyWebSocketClient webSocketClient;
    private Disposable subscription;

    public BinanceWebSocketClient(BinanceProperties binanceProperties, MarketMessageHandler marketMessageHandler) {
        this.binanceProperties = binanceProperties;
        this.marketMessageHandler = marketMessageHandler;
        this.webSocketClient = new ReactorNettyWebSocketClient();
    }
    @PreDestroy
    public void stop() {
        if (subscription != null && !subscription.isDisposed()) {
            subscription.dispose();
        }
    }
    @PostConstruct
    public void openConnect(){
        String url=binanceProperties.buildCombinedTradeStreamUrl();
        subscription=Mono.defer(()->connectOnce(url))
                .doOnSubscribe(s->log.info("Started Binance WS client : {}",url))
                .retryWhen(
                        Retry.fixedDelay(Long.MAX_VALUE, Duration.ofSeconds(5))
                                .doBeforeRetry(signal->{
                                    log.warn("Binance WS reconnect attempt: {}",signal.totalRetries()+1);
                                })
                )
                .subscribe(null,error->log.error("Binance ws stoppedd with error",error));
    }

    private Mono<Void> connectOnce(String url){
        return webSocketClient.execute(
                URI.create(url),
                session -> {
                    log.info("Connected to binance");
                    return session.receive().map(WebSocketMessage::getPayloadAsText)
                            .doOnNext(marketMessageHandler::handle)
                            .doOnError(error -> log.error("Error on opnening connection to binance: {}", error))
                            .then();
                }
                ).doOnError(e->log.error("Binance WS connection failed",e))
                .doFinally(signal->log.warn("Binance cnnections closed : {}",signal));
    }
}
