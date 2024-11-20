package com.vladyslavpalamarchuk.trackmycoin.command.processor;

import com.vladyslavpalamarchuk.trackmycoin.adaptors.telegram.TelegramBotClient;
import com.vladyslavpalamarchuk.trackmycoin.command.Command;
import com.vladyslavpalamarchuk.trackmycoin.config.TelegramBotDescription;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class NonCommandProcessor implements CommandProcessor {
  private final TelegramBotClient telegramBotClient;

  private final TelegramBotDescription telegramBotDescription;

  @Override
  public void process(Update update) {
    telegramBotClient.sendWelcomeMessage(
        update.getMessage().getChatId(), telegramBotDescription.getNon_command());
  }

  @Override
  public Command getCommand() {
    return Command.NON_COMMAND;
  }
}
