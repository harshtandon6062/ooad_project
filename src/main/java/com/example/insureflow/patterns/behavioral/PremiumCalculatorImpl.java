package com.example.insureflow.patterns.behavioral;

import java.math.BigDecimal;
import java.util.Map;

public class PremiumCalculatorImpl {
    private PremiumStrategy strategy;

    public PremiumCalculatorImpl(PremiumStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(PremiumStrategy strategy) {
        this.strategy = strategy;
    }

    public BigDecimal calculatePremium(BigDecimal basePremium, Map<String, Object> userData) {
        return strategy.calculate(basePremium, userData);
    }
}
