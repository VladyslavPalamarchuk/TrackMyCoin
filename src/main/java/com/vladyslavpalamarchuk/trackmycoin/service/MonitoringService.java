package com.vladyslavpalamarchuk.trackmycoin.service;

import com.vladyslavpalamarchuk.trackmycoin.adaptors.api.BinanceApiClient;
import com.vladyslavpalamarchuk.trackmycoin.adaptors.persistence.MonitoringRepository;
import com.vladyslavpalamarchuk.trackmycoin.adaptors.telegram.TelegramBotClient;
import com.vladyslavpalamarchuk.trackmycoin.domain.Monitoring;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MonitoringService {

  private final MonitoringRepository monitoringRepository;
  private final BinanceApiClient binanceApiClient;
  private final TelegramBotClient telegramBotClient;
  private final BigDecimal TARGET_PRICE_TOLERANCE = BigDecimal.valueOf(0.001);

  @Scheduled(fixedRateString = "${schedule.monitoring-check-rate-millis:1000}")
  public void monitor() {
    try {
      monitoringRepository.findAll().stream()
          .collect(Collectors.groupingBy(Monitoring::getTicker))
          .entrySet()
          .parallelStream()
          .forEach((entry) -> processTickerMonitorings(entry.getKey(), entry.getValue()));
    } catch (Exception e) {
      log.warn("Error during monitoring check: {}", e.getMessage(), e);
    }
  }

  private void processTickerMonitorings(String ticker, List<Monitoring> monitorings) {
    BigDecimal currentPrice =
        binanceApiClient
            .getPrice(ticker)
            .orElseThrow(() -> new RuntimeException("Unable to fetch price for ticker: " + ticker));

    monitorings.stream()
        .filter(monitoring -> isTargetPriceReached(currentPrice, monitoring.getTargetPrice()))
        .forEach(this::remove);
  }

  private void remove(Monitoring monitoring) {
    monitoringRepository.delete(monitoring);
    telegramBotClient.sendMessage(
        monitoring.getUser().getChatId(),
        String.format(
            "Target price reached for %s: %.2f 🎯",
            monitoring.getTicker(), monitoring.getTargetPrice()));
  }

  private boolean isTargetPriceReached(BigDecimal currentPrice, BigDecimal targetPrice) {
    BigDecimal tolerance = targetPrice.multiply(TARGET_PRICE_TOLERANCE);
    return currentPrice.compareTo(targetPrice.subtract(tolerance)) >= 0
        && currentPrice.compareTo(targetPrice.add(tolerance)) <= 0;
  }
}
