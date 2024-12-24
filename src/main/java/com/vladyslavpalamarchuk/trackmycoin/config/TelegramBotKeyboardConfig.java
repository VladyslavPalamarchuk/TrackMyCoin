package com.vladyslavpalamarchuk.trackmycoin.config;

import java.util.*;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

@Component
public class TelegramBotKeyboardConfig {

  public final List<String> FIRST_ROW_COMMANDS = List.of("/get", "/add");

  public final List<String> SECOND_ROW_COMMANDS = List.of("/remove", "/info");

  public ReplyKeyboardMarkup buildKeyboard() {

    var builder = ReplyKeyboardMarkup.builder();
    builder.resizeKeyboard(true);

    List<KeyboardRow> keyboard = new ArrayList<>();

    KeyboardRow row = new KeyboardRow();
    for (String command : FIRST_ROW_COMMANDS) {
      row.add(new KeyboardButton(command));
    }

    KeyboardRow row2 = new KeyboardRow();
    for (String command : SECOND_ROW_COMMANDS) {
      row2.add(new KeyboardButton(command));
    }

    keyboard.add(row);
    keyboard.add(row2);

    builder.keyboard(keyboard);

    return builder.build();
  }
}
