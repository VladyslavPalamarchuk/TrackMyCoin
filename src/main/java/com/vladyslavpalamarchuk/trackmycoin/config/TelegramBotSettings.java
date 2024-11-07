package com.vladyslavpalamarchuk.trackmycoin.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "telegram.bot")
public class TelegramBotSettings {

  private String token;
}
