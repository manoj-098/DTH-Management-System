package com.example.dthMgmtSys.service;

import com.example.dthMgmtSys.dto.ApiResponse;
import com.example.dthMgmtSys.dto.SubscriptionRequestDTO;
import com.example.dthMgmtSys.dto.SubscriptionResponseDTO;
import com.example.dthMgmtSys.dto.TransactionRequestDTO;
import com.example.dthMgmtSys.model.Device;
import com.example.dthMgmtSys.model.Plan;
import com.example.dthMgmtSys.model.Subscription;
import com.example.dthMgmtSys.model.Transaction;
import com.example.dthMgmtSys.model.User;
import com.example.dthMgmtSys.model.enums.PaymentStatus;
import com.example.dthMgmtSys.model.enums.SubscriptionStatus;
import com.example.dthMgmtSys.repository.DeviceRepository;
import com.example.dthMgmtSys.repository.PlanRepository;
import com.example.dthMgmtSys.repository.SubscriptionRepository;
import com.example.dthMgmtSys.repository.TransactionRepository;
import com.example.dthMgmtSys.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService{

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final PlanRepository planRepository;
    private final DeviceRepository deviceRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionServiceImpl transactionService;

    @Override
    public ResponseEntity<ApiResponse> getAllSubscriptions(){
        List<SubscriptionResponseDTO> subscriptionResponseDTOS = convertSubscriptionsToDTO(subscriptionRepository.findAll());
        if(subscriptionResponseDTOS.isEmpty())
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ApiResponse(
                            "No Data available", null, LocalDateTime.now()
                    )
            );

        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse(
                        "Data fetched successfully", subscriptionResponseDTOS, LocalDateTime.now()
                )
        );
    }

    @Override
    public ResponseEntity<ApiResponse> getSubscriptionById(Integer subscriptionId, String userName){
        User user = userRepository.findByUserName(userName).orElse(null);
        Subscription subscription = subscriptionRepository.findById(subscriptionId).orElse(null);
        if(subscription == null){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ApiResponse(
                            "No Data available", null, LocalDateTime.now()
                    )
            );
        }

        if(userName != subscription.getUser().getUserName()){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    new ApiResponse(
                            "You cannot access other user's data", null, LocalDateTime.now()
                    )
            );
        }

        SubscriptionResponseDTO subscriptionResponseDTO = convertSubscriptionToDTO(subscription);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse(
                        "Data fetched successfully", subscriptionResponseDTO, LocalDateTime.now()
                )
        );
    }

    @Override
    public ResponseEntity<ApiResponse> getSubscriptionsOfAPlanById(Integer planId, String userName){
        User user = userRepository.findByUserName(userName).orElse(null);
        Plan plan = planRepository.findById(planId).orElse(null);
        if(plan==null)
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ApiResponse(
                            "No plan found for Plan-ID ["+planId+"]", null, LocalDateTime.now()
                    )
            );

        List<Subscription> subscriptions = plan.getSubscriptions();
        List<SubscriptionResponseDTO> subscriptionResponseDTOS = convertSubscriptionsToDTO(subscriptions);
        if(subscriptionResponseDTOS.isEmpty())
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ApiResponse(
                            "No Data available", null, LocalDateTime.now()
                    )
            );

        if(Objects.equals(user.getRole().toString(), "ADMIN"))
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ApiResponse(
                            "Data fetched successfully", subscriptionResponseDTOS, LocalDateTime.now()
                    )
            );

        if(userName != plan.getProvider().getUserName())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    new ApiResponse(
                            "You cannot access other user's data", null, LocalDateTime.now()
                    )
            );

        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse(
                        "Data fetched successfully", subscriptionResponseDTOS, LocalDateTime.now()
                )
        );
    }

    @Override
    public ResponseEntity<ApiResponse> getAllMySubscriptions(String userName){
        List<Subscription> subscriptions = subscriptionRepository.findAllMySubscriptions(userName).orElse(null);
        List<SubscriptionResponseDTO> subscriptionResponseDTOS = convertSubscriptionsToDTO(subscriptions);
        if(subscriptionResponseDTOS.isEmpty())
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ApiResponse(
                            "No Data available", null, LocalDateTime.now()
                    )
            );

        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse(
                        "Data fetched successfully", subscriptionResponseDTOS, LocalDateTime.now()
                )
        );


    }

    @Override
    public ResponseEntity<ApiResponse> subscribeToAPlan(SubscriptionRequestDTO subscriptionRequestDTO, String userName){
        User user = userRepository.findByUserName(userName).orElse(null);
        Device device = deviceRepository.findById(subscriptionRequestDTO.getDeviceId()).orElse(null);
        Plan plan = planRepository.findById(subscriptionRequestDTO.getPlanId()).orElse(null);
        if(plan==null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse(
                            "Plan not exists for Plan-Id ["+subscriptionRequestDTO.getPlanId()+"]",
                            null, LocalDateTime.now()
                    )
            );
        if(device==null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse(
                            "Device not exists for Device-Id ["+subscriptionRequestDTO.getDeviceId()+"]",
                            null, LocalDateTime.now()
                    )
            );

        Subscription subscription = new Subscription();
        subscription.setSubscriptionStatus(SubscriptionStatus.PAYMENT_PENDING);
        subscription.setDevice(device);
        subscription.setUser(user);
        subscription.setPlan(plan);
        subscriptionRepository.save(subscription);

        // Update the Plan
        List<Subscription> subscriptionsForThePlan = plan.getSubscriptions();
        subscriptionsForThePlan.add(subscription);
        plan.setSubscriptions(subscriptionsForThePlan);

        // Making transaction after saving subscription
        TransactionRequestDTO transactionRequestDTO = new TransactionRequestDTO();
        transactionRequestDTO.setAmount(plan.getPrice());
        transactionRequestDTO.setDateTime(LocalDateTime.now());
        transactionRequestDTO.setSubscriptionId(subscription.getId());
        ResponseEntity<ApiResponse> transactionResponse = transactionService.addTransaction(transactionRequestDTO, userName);
        String message = transactionResponse.getBody().getMessage();
        int start = message.indexOf('[') + 1;
        int end = message.indexOf(']');
        String transactionId = message.substring(start, end);
        Integer transId = Integer.parseInt(transactionId);
        Transaction transaction = transactionRepository.findById(transId).orElse(null);
        if(transaction == null)
            return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body(
                new ApiResponse("Error occurred in transaction. Transaction is null", null, LocalDateTime.now())
            );

        // Update the subscription if payment is success
        if(transaction.getPaymentStatus() == PaymentStatus.SUCCESS) {
            subscription.setSubscriptionStatus(SubscriptionStatus.ACTIVE);
            subscription.setStartDate(LocalDate.now());
            subscription.setEndDate(subscription.getStartDate().plusDays(plan.getValidity()));
            subscriptionRepository.save(subscription);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse(
                    "Subscribed to Plan-Id ["+plan.getId()+"] successfully", null, LocalDateTime.now()
                )
            );
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(
            new ApiResponse(
                "Your subscription with Subscription-Id ["+ subscription.getId() +"] has been failed due to transaction failure. Please complete your transaction",
                null, LocalDateTime.now()
            )
        );

    }

    @Override
    public ResponseEntity<ApiResponse> updateSubscriptionDetails(Integer subscriptionId, SubscriptionResponseDTO subscriptionResponseDTO){
        Subscription subscription = subscriptionRepository.findById(subscriptionId).orElse(null);
        if(subscription == null)
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ApiResponse("Subscription not found for Subscription-Id ["+subscriptionId+"]",
                            null, LocalDateTime.now())
            );

        Plan plan = planRepository.findById(subscriptionResponseDTO.getPlanId()).orElse(null);
        Device device = deviceRepository.findById(subscriptionResponseDTO.getDeviceId()).orElse(null);
        User user = userRepository.findByUserName(subscriptionResponseDTO.getUserName()).orElse(null);
        if(plan == null)
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ApiResponse("Plan not found for Plan-Id ["+subscriptionResponseDTO.getPlanId()+"]",
                            null, LocalDateTime.now())
            );
        if(device == null)
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ApiResponse("Device not found for Device-Id ["+subscriptionResponseDTO.getDeviceId()+"]",
                            null, LocalDateTime.now())
            );
        if(user == null)
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ApiResponse("User not found for User-Name ["+subscriptionResponseDTO.getUserName()+"]",
                            null, LocalDateTime.now())
            );
        subscription.setPlan(plan);
        subscription.setStartDate(subscriptionResponseDTO.getStartDate());
        subscription.setEndDate(subscriptionResponseDTO.getEndDate());
        subscription.setSubscriptionStatus(subscriptionResponseDTO.getSubscriptionStatus());
        subscriptionRepository.save(subscription);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse("Subscription with Subscription-Id ["+subscriptionId+"] updated successfully",
                        null, LocalDateTime.now())
        );
    }

    @Override
    public ResponseEntity<ApiResponse> updateSubscriptionStatusById(Integer subscriptionId, SubscriptionStatus updatedStatus, String userName){
        Subscription subscription = subscriptionRepository.findById(subscriptionId).orElse(null);
        if(subscription == null)
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ApiResponse("Subscription not found for Subscription-Id ["+subscriptionId+"]",
                            null, LocalDateTime.now())
            );

        User user = userRepository.findByUserName(subscription.getPlan().getProvider().getUserName()).orElse(null);
        if(user.getUserName() != userName)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    new ApiResponse("You cannot alter the subscription data associated with a Plan provided by another Service Provider",
                            null, LocalDateTime.now())
            );

        subscription.setSubscriptionStatus(updatedStatus);
        subscriptionRepository.save(subscription);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse("Subscription status updated for Subscription-Id ["+subscriptionId+"] updated successfully",
                        null, LocalDateTime.now())
        );
    }

    @Override
    public ResponseEntity<ApiResponse> deleteSubscriptionById(Integer subscriptionId){
        Subscription subscription = subscriptionRepository.findById(subscriptionId).orElse(null);
        if(subscription == null)
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ApiResponse("Subscription not found for Subscription-Id ["+subscriptionId+"]",
                            null, LocalDateTime.now())
            );

        subscriptionRepository.deleteById(subscriptionId);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse(
                        "Subscription with Subscription-Id ["+subscriptionId+"] deleted sucessfully",
                        null, LocalDateTime.now()
                )
        );
    }

    @Override
    public List<SubscriptionResponseDTO> convertSubscriptionsToDTO(List<Subscription> subscriptions){
        if (subscriptions == null)
            return Collections.emptyList();

        return subscriptions.stream()
                .map( subscription -> {
                    SubscriptionResponseDTO dto = new SubscriptionResponseDTO();
                    dto.setId(subscription.getId());
                    dto.setUserName(subscription.getUser().getUserName());
                    dto.setPlanId(subscription.getPlan().getId());
                    dto.setSubscriptionStatus(subscription.getSubscriptionStatus());
                    dto.setDeviceId(subscription.getDevice().getId());
                    dto.setStartDate(subscription.getStartDate());
                    dto.setEndDate(subscription.getEndDate());
                    return dto;
                }).toList();
    }

    @Override
    public SubscriptionResponseDTO convertSubscriptionToDTO(Subscription subscription){
        SubscriptionResponseDTO dto = new SubscriptionResponseDTO();
        dto.setId(subscription.getId());
        dto.setUserName(subscription.getUser().getUserName());
        dto.setPlanId(subscription.getPlan().getId());
        dto.setSubscriptionStatus(subscription.getSubscriptionStatus());
        dto.setDeviceId(subscription.getDevice().getId());
        dto.setStartDate(subscription.getStartDate());
        dto.setEndDate(subscription.getEndDate());
        return dto;
    }
}
