package com.vladyslavpalamarchuk.trackmycoin.adaptors.telegram;

import com.vladyslavpalamarchuk.trackmycoin.service.MessageHandlerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramBotConsumer implements LongPollingSingleThreadUpdateConsumer {

  @Autowired
  private final MessageHandlerService messageHandlerService;

  @Override
  public void consume(Update update) {
    if (update.hasMessage() && update.getMessage().hasText()) {
      messageHandlerService.processMessage(update.getMessage().getChatId(), update.getMessage().getText() );
      logUpdateMessage(update.getMessage());
    }
  }

  private void logUpdateMessage(Message message) {
    log.debug(
        "New message: {} from username: {} chatId: {}",
        message.getText(),
        message.getFrom().getUserName(),
        message.getChatId());
  }
}
