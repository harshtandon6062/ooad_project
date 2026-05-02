package com.example.insureflow.service;

import com.example.insureflow.model.Claim;
import com.example.insureflow.model.ClaimStatus;
import com.example.insureflow.model.Policy;
import com.example.insureflow.model.PolicyStatus;
import com.example.insureflow.repository.ClaimRepository;
import com.example.insureflow.repository.PolicyRepository;
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

    public Map<String, Object> getUserDashboard(Long userId) {
        Map<String, Object> data = new HashMap<>();
        
        List<Policy> policies = policyRepository.findByUserIdAndStatusNot(userId, PolicyStatus.TEMPLATE);
        List<Claim> claims = claimRepository.findByUserId(userId);

        long activePoliciesCount = policies.stream()
            .filter(p -> p.getStatus() == PolicyStatus.ACTIVE).count();
        long pendingClaimsCount = claims.stream()
            .filter(c -> c.getStatus() == ClaimStatus.SUBMITTED).count();

        data.put("activePoliciesCount", activePoliciesCount);
        data.put("pendingClaimsCount", pendingClaimsCount);
        data.put("totalPolicies", policies.size());
        data.put("policies", policies);
        data.put("claims", claims);

        return data;
    }
}
