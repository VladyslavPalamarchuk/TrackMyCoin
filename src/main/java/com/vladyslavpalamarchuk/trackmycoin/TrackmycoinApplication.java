package com.vladyslavpalamarchuk.trackmycoin;

import com.vladyslavpalamarchuk.trackmycoin.config.TelegramBotSettings;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(TelegramBotSettings.class)
public class TrackmycoinApplication {

  public static void main(String[] args) {
    SpringApplication.run(TrackmycoinApplication.class, args);
  }
}
