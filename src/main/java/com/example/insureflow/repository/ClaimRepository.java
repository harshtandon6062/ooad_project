package com.example.insureflow.repository;

import com.example.insureflow.model.Claim;
import com.example.insureflow.model.ClaimStatus;
import com.example.insureflow.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClaimRepository extends JpaRepository<Claim, Long> {

    List<Claim> findByUser(User user);

    List<Claim> findAll();

    List<Claim> findByUserAndStatus(User user, ClaimStatus status);

    List<Claim> findByStatus(ClaimStatus status);
}