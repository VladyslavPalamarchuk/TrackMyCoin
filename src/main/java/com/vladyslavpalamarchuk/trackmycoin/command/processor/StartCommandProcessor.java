package com.vladyslavpalamarchuk.trackmycoin.command.processor;

import com.vladyslavpalamarchuk.trackmycoin.adaptors.telegram.TelegramBotClient;
import com.vladyslavpalamarchuk.trackmycoin.command.Command;
import com.vladyslavpalamarchuk.trackmycoin.config.TelegramBotDescription;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class StartCommandProcessor implements CommandProcessor {

  private final TelegramBotClient telegramBotClient;

  private final TelegramBotDescription telegramBotDescription;

  @Override
  public void process(Update update) {
    telegramBotClient.sendMessage(
        update.getMessage().getChatId(), telegramBotDescription.getStart());
  }

  @Override
  public Command getCommand() {
    return Command.START;
  }
}
