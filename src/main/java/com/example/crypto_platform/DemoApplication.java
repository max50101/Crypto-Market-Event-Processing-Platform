package com.example.crypto_platform;

import com.example.crypto_platform.market.config.BinanceProperties;
import com.example.crypto_platform.notification.email.resend.ResendProperties;
import com.example.crypto_platform.telegram.config.TelegramProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableConfigurationProperties({BinanceProperties.class, ResendProperties.class, TelegramProperties.class})
@EnableScheduling
@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
