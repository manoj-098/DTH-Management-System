package com.example.dthMgmtSys.service;

import com.example.dthMgmtSys.dto.ApiResponse;
import com.example.dthMgmtSys.dto.PasswordResetDTO;
import com.example.dthMgmtSys.dto.UserDTO;
import com.example.dthMgmtSys.model.User;
import com.example.dthMgmtSys.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserDetailsService, UserService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

    // Fetch user using email instead of username
    // Long userId = Long.parseLong(id);
    User user = userRepository.findByUserName(userName)
        .orElseThrow(
            () -> new UsernameNotFoundException("User not found with username: " + userName));

    return new org.springframework.security.core.userdetails.User(
        user.getUserName(), // this becomes the "username" Spring Security uses internally
        user.getPassword(),
        Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
    );
  }

  @Override
  public ResponseEntity<ApiResponse> registerUser(UserDTO userDTO) {
    User user = new User();
    user.setUserName(userDTO.getUserName());
    user.setEmail(userDTO.getEmail());
    user.setPassword(userDTO.getPassword());
    user.setRole(userDTO.getRole());
    userRepository.save(user);
    String message = "Welcome " + userDTO.getUserName()
        + " !\nThanks for signing-up, pls do remember your username & password";
    return ResponseEntity.status(HttpStatus.OK).body(
        new ApiResponse(
            message, null, LocalDateTime.now()
        )
    );
  }

  @Override
  public ResponseEntity<ApiResponse> resetPassword(PasswordResetDTO passwordResetDTO) {
    User user = userRepository.findByUserName(passwordResetDTO.getUserName()).orElse(null);
    if (user != null) {
      if (Objects.equals(user.getEmail(), passwordResetDTO.getEmail())) {
        user.setPassword(passwordResetDTO.getPassword());
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body(
            new ApiResponse(
                "Password updated successfully for user " + passwordResetDTO.getUserName(),
                null, LocalDateTime.now()
            )
        );
      } else {
        return ResponseEntity.status(HttpStatus.OK).body(
            new ApiResponse(
                "Incorrect EmailId. Please do enter the correct EmailId", null, LocalDateTime.now()
            )
        );
      }
    }
    return ResponseEntity.status(HttpStatus.OK).body(
        new ApiResponse(
            "No user found with username " + passwordResetDTO.getUserName(), null,
            LocalDateTime.now()
        )
    );
  }

  @Override
  public ResponseEntity<ApiResponse> getAllUsers(){
    List<User> users = userRepository.findAll();
    List<UserDTO> userDTOS = convertUsersToDTO(users);
    if(userDTOS == null)
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
          new ApiResponse(
              "No data available",
              null, LocalDateTime.now()
          )
      );

    return ResponseEntity.status(HttpStatus.OK).body(
        new ApiResponse(
            "Data fetched successfully", userDTOS, LocalDateTime.now()
        )
    );
  }

  @Override
  public ResponseEntity<ApiResponse> getUsersByUserName(String userName){
    User user = userRepository.findByUserName(userName).orElse(null);
    UserDTO userDTO = convertUserToDTO(user);
    if(userDTO == null)
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
          new ApiResponse(
              "No data available",
              null, LocalDateTime.now()
          )
      );

    return ResponseEntity.status(HttpStatus.OK).body(
        new ApiResponse(
            "Data fetched successfully", userDTO, LocalDateTime.now()
        )
    );
  }

  @Override
  public ResponseEntity<ApiResponse> updateUserByUserName(String userName, UserDTO userDTO){
    User user = userRepository.findByUserName(userName).orElse(null);
    if(user == null)
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
          new ApiResponse(
              "User not found with UserName ["+ userName +"]",
              null, LocalDateTime.now()
          )
      );

    user.setUserName(userDTO.getUserName());
    user.setEmail(userDTO.getEmail());
    user.setPassword(userDTO.getPassword());
    user.setRole(userDTO.getRole());
    userRepository.save(user);

    return ResponseEntity.status(HttpStatus.OK).body(
        new ApiResponse(
            "User details with UserName ["+ userName +"] updated successfully",
            null, LocalDateTime.now()
        )
    );
  }

  @Override
  public ResponseEntity<ApiResponse> deleteUserByUserName(String userName){
    User user = userRepository.findByUserName(userName).orElse(null);
    if(user == null)
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
          new ApiResponse(
              "User not found with UserName ["+ userName +"]",
              null, LocalDateTime.now()
          )
      );

    userRepository.delete(user);

    return ResponseEntity.status(HttpStatus.OK).body(
        new ApiResponse(
            "User with UserName ["+ userName +"] deleted successfully",
            null, LocalDateTime.now()
        )
    );
  }

  @Override
  public List<UserDTO> convertUsersToDTO(List<User> users){
    if(users == null)
        return Collections.emptyList();

    return users.stream().
        map(user -> {
          UserDTO dto = new UserDTO();
          dto.setUserName(user.getUserName());
          dto.setEmail(user.getEmail());
          dto.setPassword(user.getPassword());
          dto.setRole(user.getRole());
          return dto;
        }).toList();
  }

  @Override
  public UserDTO convertUserToDTO(User user){
    if(user == null)
      return null;

    UserDTO dto = new UserDTO();
    dto.setUserName(user.getUserName());
    dto.setEmail(user.getEmail());
    dto.setPassword(user.getPassword());
    dto.setRole(user.getRole());
    return dto;
  }
}
