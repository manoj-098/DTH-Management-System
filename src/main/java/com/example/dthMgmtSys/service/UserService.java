package com.example.dthMgmtSys.service;

import com.example.dthMgmtSys.dto.ApiResponse;
import com.example.dthMgmtSys.dto.PasswordResetDTO;
import com.example.dthMgmtSys.dto.UserDTO;
import com.example.dthMgmtSys.model.User;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface UserService {

  ResponseEntity<ApiResponse> registerUser(UserDTO userDTO);

  ResponseEntity<ApiResponse> resetPassword(PasswordResetDTO passwordResetDTO);

  ResponseEntity<ApiResponse> getAllUsers();

  ResponseEntity<ApiResponse> getUsersByUserName(String userName);

  ResponseEntity<ApiResponse> updateUserByUserName(String userName, UserDTO userDTO);

  ResponseEntity<ApiResponse> deleteUserByUserName(String userName);

  List<UserDTO> convertUsersToDTO(List<User> users);

  UserDTO convertUserToDTO(User user);
}
