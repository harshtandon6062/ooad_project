package com.example.insureflow.repository;

import com.example.insureflow.model.Policy;
import com.example.insureflow.model.PolicyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PolicyRepository extends JpaRepository<Policy, Long> {
    List<Policy> findByStatus(PolicyStatus status);
    
    @Query("SELECT p FROM Policy p WHERE p.user.userId = :userId AND p.status <> :status")
    List<Policy> findByUserIdAndStatusNot(@Param("userId") Long userId, @Param("status") PolicyStatus status);
    
    @Query("SELECT p FROM Policy p WHERE p.user.userId = :userId AND p.status = :status")
    List<Policy> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") PolicyStatus status);
    
    @Query("SELECT p FROM Policy p WHERE p.user.userId = :userId")
    List<Policy> findByUserId(@Param("userId") Long userId);
}
