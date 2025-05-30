package com.example.dthMgmtSys.service;

import com.example.dthMgmtSys.dto.ApiResponse;
import com.example.dthMgmtSys.dto.TransactionRequestDTO;
import com.example.dthMgmtSys.dto.TransactionResponseDTO;
import com.example.dthMgmtSys.model.Transaction;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface TransactionService {

  ResponseEntity<ApiResponse> getAllTransactions(String userName);

  ResponseEntity<ApiResponse> getTransactionById(String userName, Integer transactionId);

  ResponseEntity<ApiResponse> addTransaction(
      TransactionRequestDTO transactionRequestDTO, String userName);

  ResponseEntity<ApiResponse> updateTransaction(
      Integer transactionId, TransactionRequestDTO transactionRequestDTO);

  ResponseEntity<ApiResponse> deleteTransactionById(Integer transactionId);

  List<TransactionResponseDTO> convertTransactionsToDTO(List<Transaction> transactions);

  TransactionResponseDTO convertTransactionToDTO(Transaction transaction);
}
