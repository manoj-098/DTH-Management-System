package com.example.dthMgmtSys.repository;

import com.example.dthMgmtSys.model.User;
import com.example.dthMgmtSys.model.enums.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUserName(String userName);

    @Query("SELECT U.role FROM User U WHERE U.userName = :userName")
    Role findRoleByUserName(@Param("userName") String userName);

    @Query("SELECT U.id FROM User U WHERE U.userName = :userName")
    Integer findUserIdByUserName(@Param("userName") String userName);
}
