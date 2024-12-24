package com.vladyslavpalamarchuk.trackmycoin.service.command.processor;

import com.vladyslavpalamarchuk.trackmycoin.adaptors.persistence.UserRepository;
import com.vladyslavpalamarchuk.trackmycoin.adaptors.telegram.TelegramBotClient;
import com.vladyslavpalamarchuk.trackmycoin.config.TelegramBotDescription;
import com.vladyslavpalamarchuk.trackmycoin.config.TelegramBotKeyboardConfig;
import com.vladyslavpalamarchuk.trackmycoin.domain.User;
import com.vladyslavpalamarchuk.trackmycoin.service.command.Command;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartCommandProcessor implements CommandProcessor {

  private final TelegramBotClient telegramBotClient;
  private final TelegramBotDescription telegramBotDescription;
  private final UserRepository userRepository;
  private final TelegramBotKeyboardConfig telegramBotKeyboardConfig;

  @Override
  public void process(Update update) {
    Long chatId = update.getMessage().getChatId();
    ReplyKeyboardMarkup keyboardMarkup = telegramBotKeyboardConfig.buildKeyboard();

    userRepository
        .findByChatId(chatId)
        .ifPresentOrElse(
            user -> {
              telegramBotClient.sendMessageWithKeyboard(
                  chatId, telegramBotDescription.getStart(), keyboardMarkup);
            },
            () -> {
              User newUser = new User();
              newUser.setChatId(chatId);
              newUser.setCreatedBy("bot");
              newUser.setUpdatedBy("bot");
              userRepository.save(newUser);
              telegramBotClient.sendMessageWithKeyboard(
                  chatId, telegramBotDescription.getStart(), keyboardMarkup);
            });
  }

  @Override
  public Command getCommand() {
    return Command.START;
  }
}
