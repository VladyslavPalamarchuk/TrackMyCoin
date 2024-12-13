package com.vladyslavpalamarchuk.trackmycoin.command.processor;

import com.vladyslavpalamarchuk.trackmycoin.adaptors.persistence.UserRepository;
import com.vladyslavpalamarchuk.trackmycoin.adaptors.telegram.TelegramBotClient;
import com.vladyslavpalamarchuk.trackmycoin.command.Command;
import com.vladyslavpalamarchuk.trackmycoin.config.TelegramBotDescription;
import com.vladyslavpalamarchuk.trackmycoin.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartCommandProcessor implements CommandProcessor {

  private final TelegramBotClient telegramBotClient;
  private final TelegramBotDescription telegramBotDescription;
  private final UserRepository userRepository;

  @Override
  public void process(Update update) {
    Long chatId = update.getMessage().getChatId();

    userRepository
        .findByChatId(chatId)
        .ifPresentOrElse(
            user -> {
              telegramBotClient.sendMessage(chatId, telegramBotDescription.getStart());
            },
            () -> {
              User newUser = new User();
              newUser.setChatId(chatId);
              newUser.setCreatedBy("bot");
              newUser.setUpdatedBy("bot");
              userRepository.save(newUser);
              telegramBotClient.sendMessage(chatId, telegramBotDescription.getStart());
            });
  }

  @Override
  public Command getCommand() {
    return Command.START;
  }
}
