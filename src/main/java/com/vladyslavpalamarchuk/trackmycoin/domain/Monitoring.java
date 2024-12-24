package com.vladyslavpalamarchuk.trackmycoin.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "monitorings")
@Getter
@Setter
@NoArgsConstructor
public class Monitoring extends AuditableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "ticker", nullable = false, length = 100)
  private String ticker;

  @Column(name = "target_price", nullable = false, precision = 18, scale = 8)
  private BigDecimal targetPrice;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(
      name = "user_id",
      nullable = false,
      foreignKey = @ForeignKey(name = "fk_monitorings_users"))
  private User user;
}
