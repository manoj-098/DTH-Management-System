package com.example.dthMgmtSys.model;

import com.example.dthMgmtSys.model.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(unique = true, nullable = false)
  private String userName;

  private String email;
  private String password;

  @Enumerated(EnumType.STRING)
  private Role role;

  // Optional : We can also maintain a list of Devices, Transactions, Subscriptions, Tickets for a user.
  // Since, we have planned to query the child tables & don't disturb the parent table, we haven't defined those lists here
}
