package com.vladyslavpalamarchuk.trackmycoin.adaptors.telegram;

import com.vladyslavpalamarchuk.trackmycoin.command.CommandProcessorRegistry;
import com.vladyslavpalamarchuk.trackmycoin.command.processor.CommandProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramBotConsumer implements LongPollingSingleThreadUpdateConsumer {

  private final CommandProcessorRegistry commandProcessorRegistry;

  @Override
  public void consume(Update update) {
    if (update.hasMessage() && update.getMessage().hasText()) {
      CommandProcessor processMessage = commandProcessorRegistry.get(update.getMessage().getText());
      processMessage.process(update);
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
