package com.example.dthMgmtSys.dto;

import com.example.dthMgmtSys.model.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

  @NotBlank(message = "Username is required")
  private String userName;

  @NotBlank(message = "Email is required")
  @Email(message = "Invalid email, pls enter valid email")
  private String email;

  @NotBlank(message = "Password is required")
  private String password;

  @NotNull(message = "Role is required")
  private Role role;
}
