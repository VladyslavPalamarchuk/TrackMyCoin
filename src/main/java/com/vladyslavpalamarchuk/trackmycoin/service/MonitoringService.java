package com.vladyslavpalamarchuk.trackmycoin.service;

import com.vladyslavpalamarchuk.trackmycoin.adaptors.api.BinanceApiClient;
import com.vladyslavpalamarchuk.trackmycoin.adaptors.persistence.MonitoringRepository;
import com.vladyslavpalamarchuk.trackmycoin.adaptors.persistence.UserRepository;
import com.vladyslavpalamarchuk.trackmycoin.adaptors.telegram.TelegramBotClient;
import com.vladyslavpalamarchuk.trackmycoin.domain.Monitoring;
import com.vladyslavpalamarchuk.trackmycoin.domain.User;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class MonitoringService {

  private final MonitoringRepository monitoringRepository;
  private final TelegramBotClient telegramBotClient;
  private final UserRepository userRepository;
  private final BinanceApiClient binanceApiClient;
  private final NotificationService notificationService;
  private final String USER_PREFIX = "User_";
  private final String TICKER_CURRENCY = "USDT";
  private final BigDecimal DECIMAL_FORMAT_TYPE = BigDecimal.valueOf(1);

  public void add(String ticker, String price, long chatId) {
    Monitoring monitoring = new Monitoring();
    monitoring.setTicker(ticker.toUpperCase() + TICKER_CURRENCY);

    BigDecimal targetPrice = new BigDecimal(price);
    monitoring.setTargetPrice(targetPrice);

    User user = userRepository.findUserByChatId(chatId);
    monitoring.setUser(user);
    monitoring.setCreatedBy(USER_PREFIX + chatId);
    monitoring.setUpdatedBy(USER_PREFIX + chatId);
    monitoringRepository.save(monitoring);

    telegramBotClient.sendMessage(chatId, "The coin is available! Monitoring successfully added.");
  }

  public String get(Long userId) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(
                () -> new IllegalArgumentException("User with ID " + userId + " not found"));

    List<Monitoring> monitorings = monitoringRepository.findByUserId(userId);

    if (monitorings.isEmpty()) {
      telegramBotClient.sendMessage(user.getChatId(), "You have no active monitorings.");
      return null;
    }

    StringBuilder message = new StringBuilder("Your active monitorings:\n\n");

    for (Monitoring monitoring : monitorings) {
      if (DECIMAL_FORMAT_TYPE.compareTo(monitoring.getTargetPrice()) > 0) {
        message.append(
            String.format(
                "%s: Target price %.8f\n\n", monitoring.getTicker(), monitoring.getTargetPrice()));
      } else {
        message.append(
            String.format(
                "%s: Target price %.2f\n\n", monitoring.getTicker(), monitoring.getTargetPrice()));
      }
    }

    telegramBotClient.sendMessage(user.getChatId(), message.toString());
    return null;
  }

  @Scheduled(fixedRate = 1000)
  public void monitoringCheckTask() {
    try {
      monitoringCheck();
    } catch (Exception e) {
      log.warn("Error during monitoring check: {}", e.getMessage(), e);
    }
  }

  public void monitoringCheck() {
    List<Monitoring> monitorings = monitoringRepository.findAll();

    if (monitorings.isEmpty()) {
      return;
    }

    Map<String, List<Monitoring>> tickerToMonitorings =
        monitorings.stream().collect(Collectors.groupingBy(Monitoring::getTicker));

    tickerToMonitorings.forEach(
        (ticker, monitoringList) -> {
          Optional<BigDecimal> currentPrice = binanceApiClient.getPrice(ticker);

          currentPrice.ifPresent(
              price -> {
                monitoringList.forEach(
                    monitoring -> {
                      if (isTargetPriceReached(price, monitoring.getTargetPrice())) {
                        notificationService.notifyUser(monitoring);
                      }
                    });
              });
        });
    tickerToMonitorings.clear();
  }

  private boolean isTargetPriceReached(BigDecimal currentPrice, BigDecimal targetPrice) {
    BigDecimal percentRange = targetPrice.multiply(new BigDecimal("0.001"));
    BigDecimal lowerBound = targetPrice.subtract(percentRange);
    BigDecimal upperBound = targetPrice.add(percentRange);

    return currentPrice.compareTo(lowerBound) >= 0 && currentPrice.compareTo(upperBound) <= 0;
  }

}
