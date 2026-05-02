package com.example.insureflow.service;

import com.example.insureflow.model.Policy;
import com.example.insureflow.model.PolicyStatus;
import com.example.insureflow.model.User;
import com.example.insureflow.repository.PolicyRepository;
import com.example.insureflow.repository.UserRepository;
import com.example.insureflow.patterns.creational.PolicyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PolicyService {
    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Policy> searchTemplates() {
        return policyRepository.findByStatus(PolicyStatus.TEMPLATE);
    }

    public Optional<Policy> getPolicyById(Long policyId) {
        return policyRepository.findById(policyId);
    }

    public List<Policy> getUserPolicies(Long userId) {
        return policyRepository.findByUserIdAndStatusNot(userId, PolicyStatus.TEMPLATE);
    }

    public Policy createTemplate(String type, String provider, BigDecimal premium, BigDecimal coverageAmount) {
        Policy policy = PolicyFactory.createTemplate(type, provider, premium, coverageAmount);
        return policyRepository.save(policy);
    }

    public Policy purchasePolicy(Long userId, Long templateId, LocalDate startDate, LocalDate expiryDate) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Policy> template = policyRepository.findById(templateId);

        if (user.isEmpty() || template.isEmpty()) {
            throw new RuntimeException("User or Policy template not found");
        }

        Policy newPolicy = PolicyFactory.issuePolicy(
            template.get().getType(),
            template.get().getProvider(),
            template.get().getPremium(),
            template.get().getCoverageAmount(),
            startDate,
            expiryDate
        );
        newPolicy.setUser(user.get());
        return policyRepository.save(newPolicy);
    }

    public Policy save(Policy policy) {
        return policyRepository.save(policy);
    }
}
