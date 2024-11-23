package com.vladyslavpalamarchuk.trackmycoin.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "description")
public class TelegramBotDescription {

  private String start;

  private String info;

  private String help;

  private String nonCommand;
}
