package com.example.insureflow.service;

import com.example.insureflow.model.Claim;
import com.example.insureflow.model.ClaimStatus;
import com.example.insureflow.model.Policy;
import com.example.insureflow.model.User;
import com.example.insureflow.repository.ClaimRepository;
import com.example.insureflow.repository.PolicyRepository;
import com.example.insureflow.repository.UserRepository;
import com.example.insureflow.patterns.creational.ClaimFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ClaimService {
    @Autowired
    private ClaimRepository claimRepository;

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private UserRepository userRepository;

    public Claim submitClaim(Long policyId, Long userId, BigDecimal amount, String description) {
        Optional<Policy> policy = policyRepository.findById(policyId);
        Optional<User> user = userRepository.findById(userId);

        if (policy.isEmpty() || user.isEmpty()) {
            throw new RuntimeException("Policy or User not found");
        }

        Claim claim = ClaimFactory.createClaim(policy.get(), user.get(), amount, description);
        return claimRepository.save(claim);
    }

    public Optional<Claim> getClaimById(Long claimId) {
        return claimRepository.findById(claimId);
    }

    public List<Claim> getUserClaims(Long userId) {
        return claimRepository.findByUserId(userId);
    }

    public List<Claim> getAllClaims() {
        return claimRepository.findAll();
    }

    public Claim updateClaimStatus(Long claimId, ClaimStatus status) {
        Optional<Claim> claim = claimRepository.findById(claimId);
        if (claim.isPresent()) {
            claim.get().setStatus(status);
            if (status == ClaimStatus.APPROVED || status == ClaimStatus.REJECTED || status == ClaimStatus.PAID) {
                claim.get().setResolvedDate(LocalDateTime.now());
            }
            return claimRepository.save(claim.get());
        }
        throw new RuntimeException("Claim not found");
    }

    public Claim save(Claim claim) {
        return claimRepository.save(claim);
    }
}
