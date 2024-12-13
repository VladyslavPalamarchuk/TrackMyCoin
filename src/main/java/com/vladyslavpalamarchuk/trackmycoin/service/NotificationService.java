package com.vladyslavpalamarchuk.trackmycoin.service;

import com.vladyslavpalamarchuk.trackmycoin.adaptors.persistence.MonitoringRepository;
import com.vladyslavpalamarchuk.trackmycoin.adaptors.telegram.TelegramBotClient;
import com.vladyslavpalamarchuk.trackmycoin.domain.Monitoring;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class NotificationService {
  private final MonitoringRepository monitoringRepository;
  private final TelegramBotClient telegramBotClient;

  @Transactional
  public void notifyUser(Monitoring monitoring) {
    BigDecimal targetPrice = monitoring.getTargetPrice();
    String message =
        String.format("Target price reached for %s: %.2f", monitoring.getTicker(), targetPrice);

    telegramBotClient.sendMessage(monitoring.getUser().getChatId(), message);
    monitoringRepository.delete(monitoring);
  }
}
