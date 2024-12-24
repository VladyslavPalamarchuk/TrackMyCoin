package com.vladyslavpalamarchuk.trackmycoin.adaptors.api.DTO;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BinancePriceResponse {
  private String symbol;
  private BigDecimal price;
}
