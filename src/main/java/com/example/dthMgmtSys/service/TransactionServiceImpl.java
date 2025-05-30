package com.example.dthMgmtSys.service;

import com.example.dthMgmtSys.dto.ApiResponse;
import com.example.dthMgmtSys.dto.TransactionRequestDTO;
import com.example.dthMgmtSys.dto.TransactionResponseDTO;
import com.example.dthMgmtSys.model.Subscription;
import com.example.dthMgmtSys.model.Transaction;
import com.example.dthMgmtSys.model.User;
import com.example.dthMgmtSys.model.enums.PaymentStatus;
import com.example.dthMgmtSys.model.enums.Role;
import com.example.dthMgmtSys.repository.SubscriptionRepository;
import com.example.dthMgmtSys.repository.TransactionRepository;
import com.example.dthMgmtSys.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

  private final TransactionRepository transactionRepository;
  private final UserRepository userRepository;
  private final SubscriptionRepository subscriptionRepository;

  @Override
  public ResponseEntity<ApiResponse> getAllTransactions(String userName){
    User user = userRepository.findByUserName(userName).orElse(null);

    List<Transaction> transactions;
    if(user.getRole() == Role.ADMIN)
        transactions = transactionRepository.findAll();
    else if(user.getRole() == Role.USER)
        transactions = transactionRepository.findTransactionsByUserName(userName).orElse(null);
    else
        transactions = transactionRepository.findTransactionsUnderServiceProvider(userName).orElse(null);


    List<TransactionResponseDTO> transactionResponseDTOS = convertTransactionsToDTO(transactions);
    if(transactionResponseDTOS == null)
      return ResponseEntity.status(HttpStatus.OK).body(
          new ApiResponse(
              "No Data found", null, LocalDateTime.now()
          )
      );

    return ResponseEntity.status(HttpStatus.OK).body(
        new ApiResponse(
            "Data fetched successfully", transactionResponseDTOS, LocalDateTime.now()
        )
    );
  }

  @Override
  public ResponseEntity<ApiResponse> getTransactionById(String userName, Integer transactionId){
    User user = userRepository.findByUserName(userName).orElse(null);

    Transaction transaction = transactionRepository.findById(transactionId).orElse(null);
    if((user.getRole() == Role.USER && transaction.getUser().getUserName() != userName) ||
       (user.getRole() == Role.SERVICE_PROVIDER && transaction.getSubscription().getPlan().getProvider().getUserName() != userName))
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
          new ApiResponse(
              "You cannot access other user's data", null, LocalDateTime.now()
          )
      );

    TransactionResponseDTO transactionResponseDTO = convertTransactionToDTO(transaction);
    if(transactionResponseDTO == null)
      return ResponseEntity.status(HttpStatus.OK).body(
          new ApiResponse(
              "No Data found", null, LocalDateTime.now()
          )
      );

    return ResponseEntity.status(HttpStatus.OK).body(
        new ApiResponse(
            "Data fetched successfully", transactionResponseDTO, LocalDateTime.now()
        )
    );
  }

  @Override
  public ResponseEntity<ApiResponse> addTransaction(TransactionRequestDTO transactionRequestDTO, String userName){
    Transaction transaction = new Transaction();
    transaction.setAmount(transactionRequestDTO.getAmount());
    transaction.setDateTime(transactionRequestDTO.getDateTime());
    transaction.setPaymentStatus(PaymentStatus.SUCCESS);

    User user = userRepository.findByUserName(userName).orElse(null);
    Subscription subscription = subscriptionRepository.findById(transactionRequestDTO.getSubscriptionId()).orElse(null);

    transaction.setUser(user);
    transaction.setSubscription(subscription);

    transactionRepository.save(transaction);

    return ResponseEntity.status(HttpStatus.CREATED).body(
        new ApiResponse(
            "Transaction created successfully with Transaction-Id [" + transaction.getId() +"]",
            null, LocalDateTime.now()
        )
    );
  }

  @Override
  public ResponseEntity<ApiResponse> updateTransaction(Integer transactionId, TransactionRequestDTO transactionRequestDTO){
    Transaction transaction = transactionRepository.findById(transactionId).orElse(null);
    if(transaction == null)
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
          new ApiResponse(
              "Transaction not found with Transaction-Id [" + transactionId +"]",
              null, LocalDateTime.now()
          )
      );

    transaction.setAmount(transactionRequestDTO.getAmount());
    transaction.setDateTime(transactionRequestDTO.getDateTime());
    transaction.setPaymentStatus(PaymentStatus.SUCCESS);

    Subscription subscription = subscriptionRepository.findById(transactionRequestDTO.getSubscriptionId()).orElse(null);
    transaction.setSubscription(subscription);

    transactionRepository.save(transaction);

    return ResponseEntity.status(HttpStatus.OK).body(
        new ApiResponse(
            "Transaction updated successfully with Transaction-Id [" + transaction.getId() +"]",
            null, LocalDateTime.now()
        )
    );
  }

  @Override
  public ResponseEntity<ApiResponse> deleteTransactionById(Integer transactionId){
    Transaction transaction = transactionRepository.findById(transactionId).orElse(null);
    if(transaction == null)
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
          new ApiResponse(
              "Transaction not found with Transaction-Id [" + transactionId +"]",
              null, LocalDateTime.now()
          )
      );

    transactionRepository.deleteById(transactionId);
    return ResponseEntity.status(HttpStatus.OK).body(
        new ApiResponse(
            "Transaction deleted successfully with Transaction-Id [" + transactionId +"]",
            null, LocalDateTime.now()
        )
    );
  }

  @Override
  public List<TransactionResponseDTO> convertTransactionsToDTO(List<Transaction> transactions){
    if(transactions == null)
      return Collections.emptyList();

    return transactions.stream().
        map( transaction -> {
          TransactionResponseDTO dto = new TransactionResponseDTO();
          dto.setId(transaction.getId());
          dto.setAmount(transaction.getAmount());
          dto.setDateTime(transaction.getDateTime());
          dto.setPaymentStatus(transaction.getPaymentStatus());
          dto.setSubscriptionId(transaction.getSubscription().getId());
          return dto;
        }).toList();
  }

  @Override
  public TransactionResponseDTO convertTransactionToDTO(Transaction transaction){
    if(transaction == null)
        return null;

    TransactionResponseDTO dto = new TransactionResponseDTO();
    dto.setId(transaction.getId());
    dto.setAmount(transaction.getAmount());
    dto.setDateTime(transaction.getDateTime());
    dto.setPaymentStatus(transaction.getPaymentStatus());
    dto.setSubscriptionId(transaction.getSubscription().getId());
    return dto;
  }

}
