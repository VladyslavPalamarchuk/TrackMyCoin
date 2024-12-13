package com.vladyslavpalamarchuk.trackmycoin.adaptors.api;

import com.vladyslavpalamarchuk.trackmycoin.adaptors.api.DTO.BinancePriceResponse;
import java.math.BigDecimal;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class BinanceApiClient {
  private final String BINANCE_URL = "https://api.binance.com/api/v3/ticker/price?symbol=";
  private final RestTemplate restTemplate = new RestTemplate();
  private final String TICKER_CURRENCY = "USDT";

  public boolean isCoinAvailable(String ticker) {
    String url = BINANCE_URL + ticker.toUpperCase() + TICKER_CURRENCY;
    try {
      restTemplate.getForEntity(url, String.class);
      return true;
    } catch (HttpClientErrorException.NotFound e) {
      log.warn("Coin not available: {}", ticker);
      return false;
    } catch (Exception e) {
      log.error("Error while checking coin availability", e);
      return false;
    }
  }

  public Optional<BigDecimal> getPrice(String ticker) {
    String url = BINANCE_URL + ticker;
    try {
      RestTemplate restTemplate = new RestTemplate();
      BinancePriceResponse response = restTemplate.getForObject(url, BinancePriceResponse.class);
      if (response != null && response.getPrice() != null) {
        return Optional.of(response.getPrice());
      } else {
        log.warn("Price not found for ticker: {}", ticker);
        return Optional.empty();
      }
    } catch (Exception e) {
      log.error("Failed to fetch price for ticker: {}. Error: {}", ticker, e.getMessage(), e);
      return Optional.empty();
    }
  }
}
