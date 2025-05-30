package com.example.dthMgmtSys.repository;

import com.example.dthMgmtSys.model.Transaction;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    @Query("SELECT T FROM Transaction T WHERE LOWER(T.user.userName) = LOWER(:userName)")
    Optional<List<Transaction>> findTransactionsByUserName(@Param("userName") String userName);

    @Query("SELECT T FROM Transaction T WHERE LOWER(T.subscription.plan.provider.userName) = LOWER(:userName)")
    Optional<List<Transaction>> findTransactionsUnderServiceProvider(@Param("userName") String userName);

}
