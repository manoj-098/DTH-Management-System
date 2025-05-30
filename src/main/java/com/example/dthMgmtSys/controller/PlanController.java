package com.example.dthMgmtSys.controller;

import com.example.dthMgmtSys.dto.ApiResponse;
import com.example.dthMgmtSys.dto.PlanRequestDTO;
import com.example.dthMgmtSys.service.PlanServiceImpl;
import jakarta.validation.Valid;
import java.util.List;
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
@RequestMapping("/dthMgmtSys/plan")
public class PlanController {

  @Autowired
  PlanServiceImpl planServiceImpl;

  @GetMapping("/plans")
  public ResponseEntity<ApiResponse> getAllPlans() {
    return planServiceImpl.getAllPlans();
  }

  @PreAuthorize("hasRole('SERVICE_PROVIDER')")
  @GetMapping("/myPlans")
  public ResponseEntity<ApiResponse> getAllMyPlans(Authentication authentication) {
    return planServiceImpl.getAllMyPlans(authentication.getName());
  }

  @GetMapping("/plans/{planId}")
  public ResponseEntity<ApiResponse> getPlanById(@PathVariable("planId") Integer planId) {
    return planServiceImpl.getPlanById(planId);
  }

  @PreAuthorize("hasRole('SERVICE_PROVIDER')")
  @PostMapping("/plan")
  public ResponseEntity<ApiResponse> addPlan(
      @RequestBody @Valid List<PlanRequestDTO> planRequestDTOs,
      Authentication authentication
  ) {
    return planServiceImpl.addPlan(planRequestDTOs, authentication.getName());
  }

  @PreAuthorize("hasAnyRole('SERVICE_PROVIDER', 'ADMIN')")
  @PutMapping("/plan/{planId}")
  public ResponseEntity<ApiResponse> updatePlanById(
      @PathVariable("planId") Integer planId,
      @RequestBody @Valid PlanRequestDTO planRequestDTO
  ) {
    return planServiceImpl.updatePlanById(planId, planRequestDTO);
  }

  @PreAuthorize("hasAnyRole('SERVICE_PROVIDER', 'ADMIN')")
  @DeleteMapping("/plan/{planId}")
  public ResponseEntity<ApiResponse> deletePlanById(@PathVariable("planId") Integer planId) {
    return planServiceImpl.deletePlanById(planId);
  }
}
