package com.example.insureflow.patterns.creational;

import com.example.insureflow.model.Claim;
import com.example.insureflow.model.Policy;
import com.example.insureflow.model.User;
import java.math.BigDecimal;

public class ClaimFactory {
    public static Claim createClaim(Policy policy, User user, BigDecimal amount, String description) {
        validateClaimInput(policy, amount);
        return new Claim(policy, user, amount, description);
    }

    private static void validateClaimInput(Policy policy, BigDecimal amount) {
        if (policy == null) {
            throw new IllegalArgumentException("Policy cannot be null");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Claim amount must be greater than zero");
        }
    }
}
