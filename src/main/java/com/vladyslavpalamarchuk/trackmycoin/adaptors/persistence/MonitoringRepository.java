package com.vladyslavpalamarchuk.trackmycoin.adaptors.persistence;

import com.vladyslavpalamarchuk.trackmycoin.domain.Monitoring;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonitoringRepository extends JpaRepository<Monitoring, Long> {

  List<Monitoring> findByUserId(Long userId);

  List<Monitoring> findByTickerAndTargetPrice(String ticker, BigDecimal targetPrice);

  List<Monitoring> findByUserChatIdAndTickerAndTargetPrice(
      Long chatId, String ticker, BigDecimal targetPrice);
}
