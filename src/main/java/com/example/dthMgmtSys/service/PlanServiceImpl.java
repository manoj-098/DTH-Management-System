package com.example.dthMgmtSys.service;

import com.example.dthMgmtSys.dto.ApiResponse;
import com.example.dthMgmtSys.dto.PlanRequestDTO;
import com.example.dthMgmtSys.dto.PlanResponseDTO;
import com.example.dthMgmtSys.model.Plan;
import com.example.dthMgmtSys.model.User;
import com.example.dthMgmtSys.repository.PlanRepository;
import com.example.dthMgmtSys.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PlanServiceImpl implements PlanService{

    private final PlanRepository planRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseEntity<ApiResponse> getAllPlans(){
        List<PlanResponseDTO> planResponseDTOS = convertPlanToPlanResponseDTO(planRepository.findAll());
        if(planResponseDTOS.isEmpty())
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ApiResponse(
                            "No data found", null, LocalDateTime.now()
                    ));
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse(
                        "Data fetched successfully", planResponseDTOS, LocalDateTime.now()
                ));
    }

    @Override
    public ResponseEntity<ApiResponse> getPlanById(Integer planId){
        Plan plan = planRepository.findById(planId).orElse(null);
        if(plan==null)
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ApiResponse(
                            "No plan found for Plan-Id [" + planId + "]",
                            null, LocalDateTime.now()
                    ));

        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse(
                        "Data Fetched Successfully",
                        convertPlanToPlanResponseDTO(plan),
                        LocalDateTime.now()));
    }

    @Override
    public ResponseEntity<ApiResponse> getAllMyPlans(String userName){
        List<Plan> plans = planRepository.findPlansByUserName(userName);
        if(plans.isEmpty())
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ApiResponse(
                            "NO Plans found for userName ["+ userName +"]",null, LocalDateTime.now()
                    )
            );

        List<PlanResponseDTO> planResponseDTOS = convertPlanToPlanResponseDTO(plans);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse(
                        "Data fetched successfully", planResponseDTOS, LocalDateTime.now()
                )
        );
    }

    @Override
    public ResponseEntity<ApiResponse> addPlan(List<PlanRequestDTO> planRequestDTOs, String userName){
        List<String> resultMessages = new ArrayList<>();
        for(PlanRequestDTO planRequestDTO: planRequestDTOs){
            Plan plan = new Plan();
            plan.setDescription(planRequestDTO.getDescription());
            plan.setValidity(planRequestDTO.getValidity());
            plan.setPrice(planRequestDTO.getPrice());
            plan.setSubscriptions(List.of());
            User user = userRepository.findByUserName(userName).orElse(null);
            if(user == null)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ApiResponse("UserName is essential", null, LocalDateTime.now())
                );
            plan.setProvider(user);
            planRepository.save(plan);
            resultMessages.add("Plan with Id ["+ plan.getId()+"] added successfully");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse(
                        resultMessages.toString(), null, LocalDateTime.now()));
    }

    @Override
    public ResponseEntity<ApiResponse> updatePlanById(Integer planId, PlanRequestDTO planRequestDTO){
        Plan plan = planRepository.findById(planId).orElse(null);
        if(plan == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ApiResponse(
                            "No Plan found with Plan-Id: ["+ planId + "]", null, LocalDateTime.now()));

        plan.setValidity(planRequestDTO.getValidity());
        plan.setPrice(planRequestDTO.getPrice());
        plan.setDescription(planRequestDTO.getDescription());
        planRepository.save(plan);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse(
                        "Plan with Id ["+ planId +"] updated successfully", null, LocalDateTime.now()));
    }

    @Override
    public ResponseEntity<ApiResponse> deletePlanById(Integer planId){
        Plan plan = planRepository.findById(planId).orElse(null);
        if(plan == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ApiResponse(
                            "No Plan found with Plan-Id: ["+ planId + "]", null, LocalDateTime.now()));

        planRepository.deleteById(planId);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse(
                        "Plan with Id ["+ planId +"] deleted successfully", null, LocalDateTime.now())
        );
    }

    @Override
    public List<PlanResponseDTO> convertPlanToPlanResponseDTO(List<Plan> plans){
        return plans.stream()
                .map(plan -> {
                    PlanResponseDTO dto = new PlanResponseDTO();
                    dto.setId(plan.getId());
                    dto.setPrice(plan.getPrice());
                    dto.setDescription(plan.getDescription());
                    dto.setValidity(plan.getValidity());
                    return dto;
                }).toList();
    }

    @Override
    public PlanResponseDTO convertPlanToPlanResponseDTO(Plan plan){
        PlanResponseDTO dto = new PlanResponseDTO();
        dto.setId(plan.getId());
        dto.setPrice(plan.getPrice());
        dto.setDescription(plan.getDescription());
        dto.setValidity(plan.getValidity());
        return dto;
    }
}
