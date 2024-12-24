package com.vladyslavpalamarchuk.trackmycoin.service.command.processor;

import com.vladyslavpalamarchuk.trackmycoin.adaptors.telegram.TelegramBotClient;
import com.vladyslavpalamarchuk.trackmycoin.config.TelegramBotDescription;
import com.vladyslavpalamarchuk.trackmycoin.service.command.Command;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class InfoCommandProcessor implements CommandProcessor {

  private final TelegramBotClient telegramBotClient;

  private final TelegramBotDescription telegramBotDescription;

  @Override
  public void process(Update update) {
    telegramBotClient.sendMessage(
        update.getMessage().getChatId(), telegramBotDescription.getInfo());
  }

  @Override
  public Command getCommand() {
    return Command.INFO;
  }
}
