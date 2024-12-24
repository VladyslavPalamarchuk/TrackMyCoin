package com.vladyslavpalamarchuk.trackmycoin.service.command.processor;

import com.vladyslavpalamarchuk.trackmycoin.adaptors.persistence.MonitoringRepository;
import com.vladyslavpalamarchuk.trackmycoin.adaptors.persistence.UserRepository;
import com.vladyslavpalamarchuk.trackmycoin.adaptors.telegram.TelegramBotClient;
import com.vladyslavpalamarchuk.trackmycoin.domain.Monitoring;
import com.vladyslavpalamarchuk.trackmycoin.service.command.Command;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class GetMonitoringCommandProcessor implements CommandProcessor {

  private final UserRepository userRepository;
  private final MonitoringRepository monitoringRepository;
  private final TelegramBotClient telegramBotClient;

  @Override
  public void process(Update update) {
    Long chatId = update.getMessage().getChatId();

    userRepository
        .findByChatId(chatId)
        .ifPresent(
            user -> {
              List<Monitoring> monitorings = monitoringRepository.findByUserId(user.getId());
              String message = buildMonitoringListMessage(monitorings);
              telegramBotClient.sendMessage(chatId, message);
            });
  }

  private String buildMonitoringListMessage(List<Monitoring> monitorings) {
    if (monitorings.isEmpty()) {
      return "You have no active monitorings 📉";
    }

    StringBuilder message = new StringBuilder("Your active monitorings: 📊\n\n");
    for (Monitoring monitoring : monitorings) {
      String formattedPrice =
          monitoring.getTargetPrice().compareTo(BigDecimal.ONE) < 0
              ? String.format("%.8f", monitoring.getTargetPrice())
              : String.format("%.2f", monitoring.getTargetPrice());

      message.append(String.format("⚜️ %s: %s\n\n", monitoring.getTicker(), formattedPrice));
    }
    return message.toString();
  }

  @Override
  public Command getCommand() {
    return Command.GET_MONITOR;
  }
}
