package com.example.dthMgmtSys.service;

import com.example.dthMgmtSys.dto.ApiResponse;
import com.example.dthMgmtSys.dto.PlanRequestDTO;
import com.example.dthMgmtSys.dto.PlanResponseDTO;
import com.example.dthMgmtSys.model.Plan;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface PlanService {

  ResponseEntity<ApiResponse> getAllPlans();

  ResponseEntity<ApiResponse> getPlanById(Integer planId);

  ResponseEntity<ApiResponse> getAllMyPlans(String userName);

  ResponseEntity<ApiResponse> addPlan(List<PlanRequestDTO> planRequestDTOs, String userName);

  ResponseEntity<ApiResponse> updatePlanById(Integer planId, PlanRequestDTO planRequestDTO);

  ResponseEntity<ApiResponse> deletePlanById(Integer planId);

  List<PlanResponseDTO> convertPlanToPlanResponseDTO(List<Plan> plans);

  PlanResponseDTO convertPlanToPlanResponseDTO(Plan plan);

}
