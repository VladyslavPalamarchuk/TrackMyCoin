package com.vladyslavpalamarchuk.trackmycoin.service.command.processor;

import com.vladyslavpalamarchuk.trackmycoin.service.command.Command;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface CommandProcessor {

  void process(Update update);

  Command getCommand();
}
