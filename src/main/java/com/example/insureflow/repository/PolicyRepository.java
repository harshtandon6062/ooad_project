package com.example.insureflow.repository;

import com.example.insureflow.model.Policy;
import com.example.insureflow.model.PolicyStatus;
import com.example.insureflow.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PolicyRepository extends JpaRepository<Policy, Long> {

    List<Policy> findByUser(User user);

    List<Policy> findByTypeAndStatus(String type, PolicyStatus status);

    List<Policy> findByUserAndStatus(User user, PolicyStatus status);

    List<Policy> findByExpiryDateBefore(LocalDate date);

    List<Policy> findByStatus(PolicyStatus status);
}