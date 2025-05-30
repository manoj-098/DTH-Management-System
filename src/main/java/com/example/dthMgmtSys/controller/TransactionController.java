package com.example.dthMgmtSys.controller;

import com.example.dthMgmtSys.dto.ApiResponse;
import com.example.dthMgmtSys.dto.TransactionRequestDTO;
import com.example.dthMgmtSys.service.TransactionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dthMgmtSys/transaction")
public class TransactionController {

  @Autowired
  TransactionServiceImpl transactionService;

  @GetMapping("/transactions")
  public ResponseEntity<ApiResponse> getAllTransactions(Authentication authentication) {
    return transactionService.getAllTransactions(authentication.getName());
  }

  @GetMapping("/transaction/{transactionId}")
  public ResponseEntity<ApiResponse> getTransactionById(
      @PathVariable("transactionId") Integer transactionId, Authentication authentication) {
    return transactionService.getTransactionById(authentication.getName(), transactionId);
  }

  @PreAuthorize("hasRole('USER')")
  @PostMapping("/transaction")
  public ResponseEntity<ApiResponse> makeTransaction(
      @RequestBody TransactionRequestDTO transactionRequestDTO, Authentication authentication) {
    return transactionService.addTransaction(transactionRequestDTO, authentication.getName());
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("/transaction/{transactionId}")
  public ResponseEntity<ApiResponse> updateTransactionById(
      @PathVariable("transactionId") Integer transactionId,
      TransactionRequestDTO transactionRequestDTO
  ) {
    return transactionService.updateTransaction(transactionId, transactionRequestDTO);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/transaction/{transactionId}")
  public ResponseEntity<ApiResponse> updateTransactionById(
      @PathVariable("transactionId") Integer transactionId
  ) {
    return transactionService.deleteTransactionById(transactionId);
  }
}
