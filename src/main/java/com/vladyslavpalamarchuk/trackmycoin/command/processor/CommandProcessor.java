package com.vladyslavpalamarchuk.trackmycoin.command.processor;

import com.vladyslavpalamarchuk.trackmycoin.command.Command;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface CommandProcessor {

  void process(Update update);

  Command getCommand();
}
