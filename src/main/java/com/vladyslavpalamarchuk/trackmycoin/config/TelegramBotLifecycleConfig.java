package com.vladyslavpalamarchuk.trackmycoin.config;

import com.vladyslavpalamarchuk.trackmycoin.adaptors.telegram.TelegramBotConsumer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

@Configuration
@RequiredArgsConstructor
public class TelegramBotLifecycleConfig {

  private final TelegramBotConsumer telegramBotConsumer;

  private final TelegramBotSettings telegramBotSettings;

  @Bean
  @DependsOn("telegramBotConsumer")
  public TelegramBotsLongPollingApplication telegramBotApplication() {
    try {
      TelegramBotsLongPollingApplication application = new TelegramBotsLongPollingApplication();
      application.registerBot(telegramBotSettings.getToken(), telegramBotConsumer);
      return application;
    } catch (Exception e) {
      throw new IllegalStateException("Fail to register telegram bot", e);
    }
  }
}
