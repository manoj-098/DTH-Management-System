package com.example.dthMgmtSys.repository;

import com.example.dthMgmtSys.model.Plan;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlanRepository extends JpaRepository<Plan, Integer> {

    @Query("SELECT P FROM Plan P WHERE LOWER(P.user.userName) = LOWER(:userName)")
    List<Plan> findPlansByUserName(@Param("userName") String userName);
}
