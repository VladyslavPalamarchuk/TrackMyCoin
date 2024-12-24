package com.vladyslavpalamarchuk.trackmycoin.service.command.processor;

import com.vladyslavpalamarchuk.trackmycoin.adaptors.api.BinanceApiClient;
import com.vladyslavpalamarchuk.trackmycoin.adaptors.persistence.MonitoringRepository;
import com.vladyslavpalamarchuk.trackmycoin.adaptors.persistence.UserRepository;
import com.vladyslavpalamarchuk.trackmycoin.adaptors.telegram.TelegramBotClient;
import com.vladyslavpalamarchuk.trackmycoin.domain.Monitoring;
import com.vladyslavpalamarchuk.trackmycoin.domain.User;
import com.vladyslavpalamarchuk.trackmycoin.service.command.Command;
import java.math.BigDecimal;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class AddMonitoringCommandProcessor implements CommandProcessor {

  private final BinanceApiClient binanceApiClient;
  private final TelegramBotClient telegramBotClient;
  private final MonitoringRepository monitoringRepository;
  private final UserRepository userRepository;

  private final String ADD_COMMAND = Command.ADD_MONITOR.getCommand();
  private final int PART_LENGTH = 2;
  private final String TICKER_CURRENCY = "USDT";
  private final String USER_PREFIX = "User_";

  @Override
  public void process(Update update) {
    Long chatId = getChatId(update);
    String userMessage = update.getMessage().getText();

    if (userMessage.equalsIgnoreCase(ADD_COMMAND)) {
      telegramBotClient.sendMessage(
          chatId, "Please enter the coin ticker and its current price\nfor example -> ETH 4000");
      return;
    }
    handleTickerInput(update);
  }

  private void handleTickerInput(Update update) {
    String[] inputParts = extractAndValidateInput(update, getChatId(update));
    if (inputParts != null) {
      processValidTicker(getChatId(update), inputParts[0], inputParts[1]);
    }
  }

  private Long getChatId(Update update) {
    return update.getMessage().getChatId();
  }

  private String[] extractAndValidateInput(Update update, Long chatId) {
    String[] parts = update.getMessage().getText().split(" ");
    if (parts.length != PART_LENGTH) {
      telegramBotClient.sendMessage(
          chatId, "Please enter two values: the coin ticker and its current price ⚠️");
      return null;
    }
    return parts;
  }

  private void processValidTicker(Long chatId, String ticker, String price) {
    if (isCoinAvailable(ticker) && formatPrice(price).isPresent()) {
      addTickerToMonitoring(ticker, formatPrice(price).get(), chatId);
    } else {
      sendCoinNotFoundMessage(chatId);
    }
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

  private boolean isCoinAvailable(String ticker) {
    return binanceApiClient.isCoinAvailable(ticker);
  }

  private void addTickerToMonitoring(String ticker, BigDecimal price, Long chatId) {
    add(ticker, price, chatId);
    telegramBotClient.sendMessage(chatId, "The coin is available! Monitoring successfully added ✅");
  }

  private void sendCoinNotFoundMessage(Long chatId) {
    telegramBotClient.sendMessage(chatId, "Coin not found. Please try again ❌");
  }

  public void add(String ticker, BigDecimal price, long chatId) {
    Monitoring monitoring = new Monitoring();
    monitoring.setTicker(ticker.toUpperCase() + TICKER_CURRENCY);
    monitoring.setTargetPrice(price);

    User user = userRepository.findByChatId(chatId).orElse(null);
    monitoring.setUser(user);
    monitoring.setCreatedBy(USER_PREFIX + chatId);
    monitoring.setUpdatedBy(USER_PREFIX + chatId);
    monitoringRepository.save(monitoring);
  }

  @Override
  public Command getCommand() {
    return Command.ADD_MONITOR;
  }
}
