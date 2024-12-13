package com.vladyslavpalamarchuk.trackmycoin.command;

import com.vladyslavpalamarchuk.trackmycoin.command.processor.CommandProcessor;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CommandProcessorRegistry {

  private final Map<Command, CommandProcessor> commandToProcessors;
  private final Map<Long, Command> userLastCommands = new ConcurrentHashMap<>();

  public CommandProcessorRegistry(List<CommandProcessor> processors) {
    commandToProcessors =
        processors.stream()
            .collect(
                Collectors.toMap(
                    com.vladyslavpalamarchuk.trackmycoin.command.processor.CommandProcessor
                        ::getCommand,
                    Function.identity()));
  }

  public CommandProcessor get(Long chatId, String messageText) {
    messageText = messageText.trim();

    if (!messageText.startsWith("/")) {
      Command lastCommand = userLastCommands.get(chatId);
      if (lastCommand == Command.ADDMONITOR) {
        return commandToProcessors.get(Command.ADDMONITOR);
      }
      return commandToProcessors.get(Command.NON_COMMAND);
    }

    // Знаходимо відповідну команду
    String finalMessageText = messageText;
    Command matchedCommand =
        Arrays.stream(Command.values())
            .filter(c -> c.getCommand().equalsIgnoreCase(finalMessageText))
            .findFirst()
            .orElse(Command.NON_COMMAND);

    userLastCommands.put(chatId, matchedCommand);

    return commandToProcessors.get(matchedCommand);
  }
}
