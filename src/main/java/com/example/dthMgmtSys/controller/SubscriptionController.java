package com.example.dthMgmtSys.controller;

import com.example.dthMgmtSys.dto.ApiResponse;
import com.example.dthMgmtSys.dto.SubscriptionRequestDTO;
import com.example.dthMgmtSys.dto.SubscriptionResponseDTO;
import com.example.dthMgmtSys.model.enums.SubscriptionStatus;
import com.example.dthMgmtSys.service.SubscriptionServiceImpl;
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
@RequestMapping("/dthMgmtSys/subscription")
public class SubscriptionController {

  @Autowired
  SubscriptionServiceImpl subscriptionService;

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/subscriptions")
  public ResponseEntity<ApiResponse> getAllSubscriptions() {
    return subscriptionService.getAllSubscriptions();
  }

  @PreAuthorize(("hasAnyRole('SERVICE_PROVIDER', 'USER')"))
  @GetMapping("/subscription/{subscriptionId}")
  public ResponseEntity<ApiResponse> getSubscriptionById(
      @PathVariable("subscriptionId") Integer subscriptionId,
      Authentication authentication
  ) {
    return subscriptionService.getSubscriptionById(subscriptionId, authentication.getName());
  }

  @PreAuthorize("hasAnyRole('SERVICE_PROVIDER', 'ADMIN')")
  @GetMapping("subscriptions/{planId}")
  public ResponseEntity<ApiResponse> getAllSubscriptionsOfAPlanById(
      @PathVariable("planId") Integer planId,
      Authentication authentication
  ) {
    return subscriptionService.getSubscriptionsOfAPlanById(planId, authentication.getName());
  }

  @PreAuthorize("hasAnyRole('SERVICE_PROVIDER', 'USER')")
  @GetMapping("/mySubscriptions")
  public ResponseEntity<ApiResponse> getAllMySubscriptions(Authentication authentication) {
    return subscriptionService.getAllMySubscriptions(authentication.getName());
  }

  @PreAuthorize("hasRole('USER')")
  @PostMapping("/subscribe")
  public ResponseEntity<ApiResponse> subscribeToAPlan(
      @RequestBody SubscriptionRequestDTO subscriptionRequestDTO,
      Authentication authentication
  ) {
    return subscriptionService.subscribeToAPlan(subscriptionRequestDTO, authentication.getName());
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("/subscription/{subscriptionId}")
  public ResponseEntity<ApiResponse> updateSubscriptionById(
      @PathVariable("subscriptionId") Integer subscriptionId,
      SubscriptionResponseDTO subscriptionResponseDTO
  ) {
    return subscriptionService.updateSubscriptionDetails(subscriptionId, subscriptionResponseDTO);
  }

  @PreAuthorize("hasAnyRole('SERVICE_PROVIDER', 'ADMIN')")
  @PutMapping("/subscriptionStatus/{subscriptionId}")
  public ResponseEntity<ApiResponse> updateSubscriptionStatusById(
      @PathVariable("subscriptionId") Integer subscriptionId,
      SubscriptionStatus updatedStatus, Authentication authentication
  ) {
    return subscriptionService.updateSubscriptionStatusById(
        subscriptionId, updatedStatus,
        authentication.getName()
    );
  }

  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/subscription/{subscriptionId}")
  public ResponseEntity<ApiResponse> deleteSubscriptionById(
      @PathVariable("subscriptionId") Integer subscriptionId
  ) {
    return subscriptionService.deleteSubscriptionById(subscriptionId);
  }

}
