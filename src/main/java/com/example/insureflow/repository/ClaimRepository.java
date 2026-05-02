package com.example.insureflow.repository;

import com.example.insureflow.model.Claim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ClaimRepository extends JpaRepository<Claim, Long> {
    @Query("SELECT c FROM Claim c WHERE c.user.userId = :userId")
    List<Claim> findByUserId(@Param("userId") Long userId);
    
    List<Claim> findAll();
}
