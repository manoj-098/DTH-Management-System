package com.example.dthMgmtSys.model;

import com.example.dthMgmtSys.model.enums.SubscriptionStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Subscription {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private LocalDate startDate;
  private LocalDate endDate;

  @Enumerated(EnumType.STRING)
  private SubscriptionStatus subscriptionStatus;

  @ManyToOne
  @JoinColumn(name = "deviceId")
  private Device device;

  @ManyToOne
  @JoinColumn(name = "userId")
  private User user;

  @ManyToOne
  @JoinColumn(name = "planId")
  private Plan plan;
}

