package com.example.insureflow.patterns.creational;

import com.example.insureflow.model.Policy;
import com.example.insureflow.model.PolicyStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class PolicyFactory {
    public static Policy createTemplate(String type, String provider, BigDecimal premium, BigDecimal coverageAmount) {
        validatePolicyType(type);
        validatePremium(premium);
        validateCoverageAmount(coverageAmount);
        Policy policy = new Policy(type, provider != null ? provider : "InsureCo", premium, coverageAmount);
        policy.setStatus(PolicyStatus.TEMPLATE);
        return policy;
    }

    public static Policy issuePolicy(String type, String provider, BigDecimal premium, 
                                     BigDecimal coverageAmount, LocalDate startDate, LocalDate expiryDate) {
        validatePolicyType(type);
        validatePremium(premium);
        validateCoverageAmount(coverageAmount);
        Policy policy = new Policy(type, provider != null ? provider : "InsureCo", premium, coverageAmount);
        policy.setStatus(PolicyStatus.ACTIVE);
        policy.setStartDate(startDate);
        policy.setExpiryDate(expiryDate);
        return policy;
    }

    private static void validatePolicyType(String type) {
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("Policy type cannot be null or empty");
        }
        List<String> validTypes = Arrays.asList("health", "auto", "home", "life");
        if (!validTypes.contains(type.toLowerCase())) {
            throw new IllegalArgumentException("Invalid policy type: " + type);
        }
    }

    private static void validatePremium(BigDecimal premium) {
        if (premium == null || premium.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Premium must be greater than zero");
        }
    }

    private static void validateCoverageAmount(BigDecimal coverageAmount) {
        if (coverageAmount == null || coverageAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Coverage amount cannot be negative");
        }
    }
}
