package com.example.dthMgmtSys.service;

import com.example.dthMgmtSys.dto.ApiResponse;
import com.example.dthMgmtSys.dto.SubscriptionRequestDTO;
import com.example.dthMgmtSys.dto.SubscriptionResponseDTO;
import com.example.dthMgmtSys.model.Subscription;
import com.example.dthMgmtSys.model.enums.SubscriptionStatus;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface SubscriptionService {

  ResponseEntity<ApiResponse> getAllSubscriptions();

  ResponseEntity<ApiResponse> getSubscriptionById(Integer subscriptionId, String userName);

  ResponseEntity<ApiResponse> getSubscriptionsOfAPlanById(Integer planId, String userName);

  ResponseEntity<ApiResponse> getAllMySubscriptions(String userName);

  ResponseEntity<ApiResponse> subscribeToAPlan(
      SubscriptionRequestDTO subscriptionRequestDTO, String userName);

  ResponseEntity<ApiResponse> updateSubscriptionDetails(
      Integer subscriptionId, SubscriptionResponseDTO subscriptionResponseDTO);

  ResponseEntity<ApiResponse> updateSubscriptionStatusById(
      Integer subscriptionId, SubscriptionStatus updatedStatus, String userName);

  ResponseEntity<ApiResponse> deleteSubscriptionById(Integer subscriptionId);

  List<SubscriptionResponseDTO> convertSubscriptionsToDTO(List<Subscription> subscriptions);

  SubscriptionResponseDTO convertSubscriptionToDTO(Subscription subscription);
}
