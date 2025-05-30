package com.example.dthMgmtSys.controller;

import com.example.dthMgmtSys.dto.ApiResponse;
import com.example.dthMgmtSys.dto.PasswordResetDTO;
import com.example.dthMgmtSys.dto.UserDTO;
import com.example.dthMgmtSys.service.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dthMgmtSys/user")
public class UserController {

  @Autowired
  UserServiceImpl userService;
  PasswordEncoder passwordEncoder;

  @PostMapping("/register")
  public ResponseEntity<ApiResponse> registerUser(@RequestBody @Valid UserDTO userDTO) {
    userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
    return userService.registerUser(userDTO);
  }

  @PostMapping("/reset_password")
  public ResponseEntity<ApiResponse> resetPassword(
      @RequestBody @Valid PasswordResetDTO passwordResetDTO
  ) {
    passwordResetDTO.setPassword(passwordEncoder.encode(passwordResetDTO.getPassword()));
    return userService.resetPassword(passwordResetDTO);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/users")
  public ResponseEntity<ApiResponse> getAllUsers() {
    return userService.getAllUsers();
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/users/{userName}")
  public ResponseEntity<ApiResponse> getUsersByUserName(@PathVariable("userName") String userName) {
    return userService.getUsersByUserName(userName);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("/users/{userName}")
  public ResponseEntity<ApiResponse> updateUsersByUserName(
      @PathVariable("userName") String userName, UserDTO userDTO) {
    return userService.updateUserByUserName(userName, userDTO);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/users/{userName}")
  public ResponseEntity<ApiResponse> deleteUsersByUserName(
      @PathVariable("userName") String userName
  ) {
    return userService.deleteUserByUserName(userName);
  }
}
