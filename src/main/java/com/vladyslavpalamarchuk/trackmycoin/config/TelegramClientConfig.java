package com.vladyslavpalamarchuk.trackmycoin.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;

@Configuration
@RequiredArgsConstructor
public class TelegramClientConfig {

  private final TelegramBotSettings telegramBotSettings;

  @Bean
  public OkHttpTelegramClient telegramClient() {
    return new OkHttpTelegramClient(telegramBotSettings.getToken());
  }
}
