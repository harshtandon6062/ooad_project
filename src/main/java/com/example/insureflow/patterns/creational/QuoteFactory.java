package com.example.insureflow.patterns.creational;

import com.example.insureflow.model.Quote;
import com.example.insureflow.model.Policy;
import com.example.insureflow.model.User;
import java.math.BigDecimal;
import java.time.LocalDate;

public class QuoteFactory {
    public static Quote createQuote(User user, Policy policy, BigDecimal premiumEstimate, LocalDate validUntil) {
        validateQuoteInput(policy, premiumEstimate);
        return new Quote(user, policy, premiumEstimate, validUntil);
    }

    private static void validateQuoteInput(Policy policy, BigDecimal premiumEstimate) {
        if (policy == null) {
            throw new IllegalArgumentException("Policy cannot be null");
        }
        if (premiumEstimate == null || premiumEstimate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Premium estimate must be greater than zero");
        }
    }
}
