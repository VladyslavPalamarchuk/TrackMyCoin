package com.vladyslavpalamarchuk.trackmycoin.adaptors.telegram;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramBotClient {

  private final TelegramClient telegramClient;

  public void sendWelcomeMessage(Long chatId, String messageText) {
    SendMessage message = SendMessage.builder().chatId(chatId).text(messageText).build();
    try {
      telegramClient.execute(message);
    } catch (TelegramApiException e) {
      log.error(e.getMessage());
    }
  }
}
