package com.vladyslavpalamarchuk.trackmycoin.command;

import com.vladyslavpalamarchuk.trackmycoin.command.processor.CommandProcessor;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CommandProcessorRegistry {

  private final Map<Command, CommandProcessor> commandToProcessors;

  public CommandProcessorRegistry(List<CommandProcessor> processors) {
    commandToProcessors =
        processors.stream()
            .collect(Collectors.toMap(CommandProcessor::getCommand, Function.identity()));
  }

  public CommandProcessor get(String messageText) {
    if (!messageText.startsWith("/")) {
      return commandToProcessors.get(Command.NON_COMMAND);
    }

    return Arrays.stream(Command.values())
        .filter(c -> c.getCommand().equalsIgnoreCase(messageText))
        .findFirst()
        .map(commandToProcessors::get)
        .orElse(commandToProcessors.get(Command.NON_COMMAND));
  }
}
