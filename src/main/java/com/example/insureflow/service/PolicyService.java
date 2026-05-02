package com.example.insureflow.service;

import com.example.insureflow.model.*;
import com.example.insureflow.patterns.behavioral.PremiumCalculator;
import com.example.insureflow.patterns.behavioral.PolicyContext;
import com.example.insureflow.patterns.creational.PolicyFactory;
import com.example.insureflow.repository.PolicyRepository;
import com.example.insureflow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class PolicyService {

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PremiumCalculator calculator;

    public Policy createPolicy(Long userId, String type, String provider, double coverageAmount, Map<String, Object> userData) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        double premium = calculator.calculatePremium(coverageAmount, userData);
        Policy policy = PolicyFactory.issuePolicy(type, provider, premium, coverageAmount, LocalDate.now(), LocalDate.now().plusYears(1));
        policy.setUser(user);
        policy.setStatus(PolicyStatus.ACTIVE);
        return policyRepository.save(policy);
    }

    public List<Policy> getUserPolicies(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return policyRepository.findByUser(user);
    }

    public Policy getPolicy(Long id) {
        return policyRepository.findById(id).orElseThrow();
    }

    public void suspendPolicy(Long id) {
        Policy policy = getPolicy(id);
        PolicyContext context = new PolicyContext(id.toString(), policy.getStatus().name().toLowerCase());
        context.suspend();
        policy.setStatus(PolicyStatus.SUSPENDED);
        policyRepository.save(policy);
    }

    public void cancelPolicy(Long id) {
        Policy policy = getPolicy(id);
        PolicyContext context = new PolicyContext(id.toString(), policy.getStatus().name().toLowerCase());
        context.cancel();
        policy.setStatus(PolicyStatus.CANCELLED);
        policyRepository.save(policy);
    }

    public void renewPolicy(Long id) {
        Policy policy = getPolicy(id);
        PolicyContext context = new PolicyContext(id.toString(), policy.getStatus().name().toLowerCase());
        context.renew();
        policy.setStatus(PolicyStatus.ACTIVE);
        policy.setExpiryDate(policy.getExpiryDate().plusYears(1));
        policyRepository.save(policy);
    }

    public List<Policy> getAvailablePolicies(String type, PolicyStatus status) {
        return policyRepository.findByTypeAndStatus(type, status);
    }

    public List<Policy> findAll() {
        return policyRepository.findAll();
    }
}