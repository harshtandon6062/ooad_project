package com.example.insureflow.service;

import com.example.insureflow.model.*;
import com.example.insureflow.repository.ClaimRepository;
import com.example.insureflow.repository.PolicyRepository;
import com.example.insureflow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private ClaimRepository claimRepository;

    @Autowired
    private UserRepository userRepository;

    public Map<String, Object> getUserDashboard(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        List<Policy> policies = policyRepository.findByUser(user);
        List<Claim> claims = claimRepository.findByUser(user);

        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("totalPolicies", policies.size());
        dashboard.put("activePolicies", policies.stream().filter(p -> p.getStatus() == PolicyStatus.ACTIVE).count());
        dashboard.put("expiredPolicies", policies.stream().filter(p -> p.getStatus() == PolicyStatus.EXPIRED).count());
        dashboard.put("cancelledPolicies", policies.stream().filter(p -> p.getStatus() == PolicyStatus.CANCELLED).count());
        dashboard.put("totalClaims", claims.size());
        dashboard.put("pendingClaims", claims.stream().filter(c -> c.getStatus() == ClaimStatus.SUBMITTED || c.getStatus() == ClaimStatus.UNDER_REVIEW).count());
        dashboard.put("approvedClaims", claims.stream().filter(c -> c.getStatus() == ClaimStatus.APPROVED).count());
        dashboard.put("paidClaims", claims.stream().filter(c -> c.getStatus() == ClaimStatus.PAID).count());
        return dashboard;
    }

    public Map<String, Object> getAdminDashboard() {
        List<Policy> allPolicies = policyRepository.findAll();
        List<Claim> allClaims = claimRepository.findAll();
        List<User> allUsers = userRepository.findAll();

        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("totalUsers", allUsers.size());
        dashboard.put("totalPolicies", allPolicies.size());
        dashboard.put("activePolicies", allPolicies.stream().filter(p -> p.getStatus() == PolicyStatus.ACTIVE).count());
        dashboard.put("totalClaims", allClaims.size());
        dashboard.put("pendingClaims", allClaims.stream().filter(c -> c.getStatus() == ClaimStatus.SUBMITTED || c.getStatus() == ClaimStatus.UNDER_REVIEW).count());
        return dashboard;
    }
}