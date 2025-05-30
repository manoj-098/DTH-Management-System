package com.example.dthMgmtSys.model;

import com.example.dthMgmtSys.model.enums.PaymentStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private double amount;
  private LocalDateTime dateTime;

  @Enumerated(EnumType.STRING)
  private PaymentStatus paymentStatus;

  @ManyToOne
  @JoinColumn(name = "userId")
  private User user;

  @ManyToOne
  @JoinColumn(name = "subscriptionId")
  private Subscription subscription;
}
