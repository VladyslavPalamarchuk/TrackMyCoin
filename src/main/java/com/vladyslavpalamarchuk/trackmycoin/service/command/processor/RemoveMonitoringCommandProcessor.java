package com.vladyslavpalamarchuk.trackmycoin.service.command.processor;

import com.vladyslavpalamarchuk.trackmycoin.adaptors.persistence.MonitoringRepository;
import com.vladyslavpalamarchuk.trackmycoin.adaptors.telegram.TelegramBotClient;
import com.vladyslavpalamarchuk.trackmycoin.domain.Monitoring;
import com.vladyslavpalamarchuk.trackmycoin.service.command.Command;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class RemoveMonitoringCommandProcessor implements CommandProcessor {

  private final TelegramBotClient telegramBotClient;
  private final MonitoringRepository monitoringRepository;

  private final String REMOVE_COMMAND = Command.REMOVE_MONITOR.getCommand();
  private final int EXPECTED_INPUT_LENGTH = 2;
  private final String TICKER_CURRENCY = "USDT";
  private List<Monitoring> Monitorings;

  @Override
  public void process(Update update) {
    Long chatId = getChatId(update);
    String userMessage = update.getMessage().getText();

    if (userMessage.equalsIgnoreCase(REMOVE_COMMAND)) {
      telegramBotClient.sendMessage(
          chatId, "Please enter the coin ticker and its current price\nfor example -> ETH 4000");
      return;
    }

    processTickerAndPriceInput(update);
  }

  private void processTickerAndPriceInput(Update update) {
    String[] inputParts = handleTickerInput(update, getChatId(update));
    if (inputParts != null) {
      remove(inputParts[0].toUpperCase() + TICKER_CURRENCY, inputParts[1], getChatId(update));
    }
  }

  private boolean isCoinAvailable(String ticker, BigDecimal price) {
    return !monitoringRepository.findByTickerAndTargetPrice(ticker, price).isEmpty();
  }

  private void remove(String ticker, String price, Long chatId) {

    Optional<BigDecimal> formattedPrice = formatPrice(price);

    if (formattedPrice.isPresent() && isCoinAvailable(ticker, formattedPrice.get())) {

      Monitorings =
          monitoringRepository.findByUser_ChatIdAndTickerAndTargetPrice(
              chatId, ticker, formattedPrice.get());
      if (Monitorings == null) {
        telegramBotClient.sendMessage(
            chatId, "The monitoring for " + ticker + " with price " + price + " was not found ❌");
      } else {
        Monitorings.stream()
            .forEach(
                monitoring -> {
                  monitoringRepository.delete(monitoring);
                  telegramBotClient.sendMessage(
                      chatId,
                      "Successfully removed "
                          + ticker
                          + " from your monitoring with target price "
                          + price
                          + "✅");
                });
      }
    } else {
      telegramBotClient.sendMessage(
          chatId, "Please enter two values: the coin ticker and its current price ⚠️");
    }
  }

  private Long getChatId(Update update) {
    return update.getMessage().getChatId();
  }

  private Optional<BigDecimal> formatPrice(String price) {
    String formatResult = price.replace(",", ".");
    if (formatResult.trim().isEmpty()) {
      return Optional.empty();
    }
    try {
      return Optional.of(new BigDecimal(formatResult));
    } catch (NumberFormatException e) {
      return Optional.empty();
    }
  }

  private String[] handleTickerInput(Update update, Long chatId) {
    String[] parts = update.getMessage().getText().split(" ");
    if (parts.length != EXPECTED_INPUT_LENGTH) {
      telegramBotClient.sendMessage(
          chatId, "Please provide two values: the coin ticker and its target price ⚠️");
      return null;
    }
    return parts;
  }

  @Override
  public Command getCommand() {
    return Command.REMOVE_MONITOR;
  }
}
