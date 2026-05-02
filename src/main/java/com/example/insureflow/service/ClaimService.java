package com.example.insureflow.service;

import com.example.insureflow.model.*;
import com.example.insureflow.patterns.behavioral.ClaimContext;
import com.example.insureflow.patterns.creational.ClaimFactory;
import com.example.insureflow.repository.ClaimRepository;
import com.example.insureflow.repository.PolicyRepository;
import com.example.insureflow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClaimService {

    @Autowired
    private ClaimRepository claimRepository;

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private UserRepository userRepository;

    public Claim submitClaim(Long userId, Long policyId, double amount, String description) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Policy policy = policyRepository.findById(policyId).orElseThrow(() -> new RuntimeException("Policy not found"));
        if (policy.getStatus() != PolicyStatus.ACTIVE) {
            throw new RuntimeException("Policy not active");
        }
        Claim claim = ClaimFactory.submitClaim(amount, description);
        claim.setUser(user);
        claim.setPolicy(policy);
        claim.setStatus(ClaimStatus.SUBMITTED);
        return claimRepository.save(claim);
    }

    public void updateClaimStatus(Long claimId, ClaimStatus status) {
        Claim claim = claimRepository.findById(claimId).orElseThrow(() -> new RuntimeException("Claim not found"));
        ClaimContext context = new ClaimContext(claimId.toString(), claim.getStatus().name().toLowerCase().replace("_", "_"));
        switch (status) {
            case UNDER_REVIEW: context.startReview(); break;
            case APPROVED: context.startReview(); context.approve(); break;
            case REJECTED: context.startReview(); context.reject(); break;
            case PAID: context.startReview(); context.approve(); context.payout(); break;
        }
        claim.setStatus(status);
        claimRepository.save(claim);
    }

    public List<Claim> getUserClaims(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return claimRepository.findByUser(user);
    }

    public List<Claim> getAllClaims() {
        return claimRepository.findAll();
    }

    public Claim getClaim(Long id) {
        return claimRepository.findById(id).orElseThrow();
    }

    public List<Claim> getClaimsByStatus(ClaimStatus status) {
        return claimRepository.findByStatus(status);
    }
}