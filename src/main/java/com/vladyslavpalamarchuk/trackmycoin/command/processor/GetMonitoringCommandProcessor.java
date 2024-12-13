package com.vladyslavpalamarchuk.trackmycoin.command.processor;

import com.vladyslavpalamarchuk.trackmycoin.adaptors.persistence.UserRepository;
import com.vladyslavpalamarchuk.trackmycoin.command.Command;
import com.vladyslavpalamarchuk.trackmycoin.service.MonitoringService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class GetMonitoringCommandProcessor implements CommandProcessor {

  private final UserRepository userRepository;
  private final MonitoringService monitoringService;

  @Override
  public void process(Update update) {
    Long chatId = update.getMessage().getChatId();

    userRepository
        .findByChatId(chatId)
        .ifPresent(
            user -> {
              monitoringService.get(user.getId());
            });
  }

  @Override
  public Command getCommand() {
    return Command.GETMONITOR;
  }
}
