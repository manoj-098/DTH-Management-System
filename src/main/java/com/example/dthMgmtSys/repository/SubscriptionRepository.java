package com.example.dthMgmtSys.repository;

import com.example.dthMgmtSys.model.Subscription;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {

    @Query("SELECT S FROM Subscription S WHERE LOWER(S.user.userName) = LOWER(:userName)")
    Optional<List<Subscription>> findAllMySubscriptions(@Param("userName") String userName);
}
