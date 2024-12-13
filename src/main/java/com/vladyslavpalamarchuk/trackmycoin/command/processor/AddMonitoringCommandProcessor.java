package com.vladyslavpalamarchuk.trackmycoin.command.processor;

import com.vladyslavpalamarchuk.trackmycoin.adaptors.api.BinanceApiClient;
import com.vladyslavpalamarchuk.trackmycoin.adaptors.telegram.TelegramBotClient;
import com.vladyslavpalamarchuk.trackmycoin.command.Command;
import com.vladyslavpalamarchuk.trackmycoin.service.MonitoringService;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class AddMonitoringCommandProcessor implements CommandProcessor {

  private final BinanceApiClient binanceApiClient;
  private final TelegramBotClient telegramBotClient;
  private final MonitoringService monitoringService;
  private final String ADD_COMMAND = "/add";
  private final int PART_LENGTH = 2;

  @Override
  public void process(Update update) {
    Long chatId = update.getMessage().getChatId();
    String userMessage = update.getMessage().getText();
    if (userMessage.equalsIgnoreCase(ADD_COMMAND)) {
      telegramBotClient.sendMessage(
          chatId, "Please enter the coin ticker and its current price (for example, BTC 50000):");
      return;
    }

    handleTickerInput(update);
  }

  private void handleTickerInput(Update update) {
    Long chatId = getChatId(update);

    String[] inputParts = extractAndValidateInput(update, chatId);

    if (inputParts != null) {
      processValidTicker(chatId, inputParts[0], inputParts[1]);
    }
  }

  private Long getChatId(Update update) {
    return update.getMessage().getChatId();
  }

  private String[] extractAndValidateInput(Update update, Long chatId) {
    String[] parts = update.getMessage().getText().split(" ");
    if (parts.length != PART_LENGTH) {
      telegramBotClient.sendMessage(
          chatId, "Please enter two values: the coin ticker and its current price.");
      return null;
    }
    return parts;
  }

  private void processValidTicker(Long chatId, String ticker, String price) {
    if (isCoinAvailable(ticker) && isPriceAvailable(price)) {
      addTickerToMonitoring(ticker, price, chatId);
    } else {
      sendCoinNotFoundMessage(chatId);
    }
  }

  private boolean isCoinAvailable(String ticker) {
    return binanceApiClient.isCoinAvailable(ticker);
  }

  private boolean isPriceAvailable(String price) {
    if (price == null || price.trim().isEmpty()) {
      return false;
    }
    try {
      new BigDecimal(price);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  private void addTickerToMonitoring(String ticker, String price, Long chatId) {
    monitoringService.add(ticker, price, chatId);
  }

  private void sendCoinNotFoundMessage(Long chatId) {
    telegramBotClient.sendMessage(chatId, "Coin not found. Please try again.");
  }

  @Override
  public Command getCommand() {
    return Command.ADDMONITOR;
  }
}
