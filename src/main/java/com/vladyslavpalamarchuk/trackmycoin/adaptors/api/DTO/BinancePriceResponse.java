package com.vladyslavpalamarchuk.trackmycoin.adaptors.api.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.Setter;

@Setter
public class BinancePriceResponse {
  private String symbol;
  private BigDecimal price;

  @JsonProperty("symbol")
  public String getSymbol() {
    return symbol;
  }

  @JsonProperty("price")
  public BigDecimal getPrice() {
    return price;
  }
}
