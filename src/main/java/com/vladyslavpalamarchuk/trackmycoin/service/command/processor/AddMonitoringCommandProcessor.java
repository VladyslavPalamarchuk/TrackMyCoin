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
  private final int EXPECTED_INPUT_LENGTH = 2;
  private final String USER_PREFIX = "User_";
  private final String USDT_TICKER = "USDT";

  @Override
  public void process(Update update) {
    Long chatId = getChatId(update);
    String userMessage = update.getMessage().getText();

    if (userMessage.equalsIgnoreCase(ADD_COMMAND)) {
      telegramBotClient.sendMessage(
          chatId, "Please enter the coin ticker and its current price.\nFor example -> ETH 4000");
      return;
    }
    handleTickerInput(update);
  }

  private void handleTickerInput(Update update) {
    String[] inputParts = extractAndValidateInput(update, getChatId(update));
    if (inputParts != null) {
      processValidTicker(getChatId(update), inputParts[0] + USDT_TICKER, inputParts[1]);
    }
  }

  private Long getChatId(Update update) {
    return update.getMessage().getChatId();
  }

  private String[] extractAndValidateInput(Update update, Long chatId) {
    String[] parts = update.getMessage().getText().split(" ");
    if (parts.length != EXPECTED_INPUT_LENGTH) {
      telegramBotClient.sendMessage(
          chatId, "Please enter two values: the coin ticker and its current price ⚠️");
      return null;
    }
    return parts;
  }

  private void processValidTicker(Long chatId, String ticker, String price) {
    Optional<BigDecimal> formatPrice = formatPrice(price);

    if (isTickerAvailable(ticker) && formatPrice.isPresent()) {
      addTickerToMonitoring(ticker, formatPrice.get(), chatId);
    } else {
      sendTickerNotFoundMessage(chatId);
    }
  }

  private boolean isTickerAvailable(String ticker) {
    return binanceApiClient.getPrice(ticker).isPresent();
  }

  private Optional<BigDecimal> formatPrice(String price) {
    String formatResult = price.replace(",", ".");
    try {
      return Optional.of(new BigDecimal(formatResult));
    } catch (NumberFormatException e) {
      return Optional.empty();
    }
  }

  private void addTickerToMonitoring(String ticker, BigDecimal price, Long chatId) {
    addMonitoring(ticker, price, chatId);
    telegramBotClient.sendMessage(chatId, "The coin is available! Monitoring successfully added ✅");
  }

  private void sendTickerNotFoundMessage(Long chatId) {
    telegramBotClient.sendMessage(chatId, "Coin not found. Please try again ❌");
  }

  private void addMonitoring(String ticker, BigDecimal price, long chatId) {
    Monitoring monitoring = new Monitoring();
    monitoring.setTicker(ticker.toUpperCase());
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
