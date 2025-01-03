package com.vladyslavpalamarchuk.trackmycoin.config;

import java.util.*;

import com.vladyslavpalamarchuk.trackmycoin.service.command.Command;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

@Component
public class TelegramBotKeyboardConfig {

  public final List<String> FIRST_ROW_COMMANDS = List.of(Command.GET_MONITOR.getCommand(), Command.ADD_MONITOR.getCommand());

  public final List<String> SECOND_ROW_COMMANDS = List.of(Command.REMOVE_MONITOR.getCommand(), Command.HELP.getCommand());

  public ReplyKeyboardMarkup buildKeyboard() {

    var builder = ReplyKeyboardMarkup.builder();
    builder.resizeKeyboard(true);

    List<KeyboardRow> keyboard = new ArrayList<>();

    KeyboardRow row = new KeyboardRow();
    FIRST_ROW_COMMANDS.stream().map(KeyboardButton::new).forEach(row::add);

    KeyboardRow row2 = new KeyboardRow();
    SECOND_ROW_COMMANDS.stream().map(KeyboardButton::new).forEach(row2::add);

    keyboard.add(row);
    keyboard.add(row2);

    builder.keyboard(keyboard);

    return builder.build();
  }
}
