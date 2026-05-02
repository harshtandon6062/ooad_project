package com.example.insureflow.patterns.behavioral;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PremiumCalculator {

    private PremiumStrategy strategy;

    public PremiumCalculator() {
        this.strategy = new StandardPremiumStrategy(); // default strategy
    }

    public PremiumCalculator(PremiumStrategy strategy) {

        this.strategy = strategy;

    }

    public void setStrategy(PremiumStrategy strategy) {

        this.strategy = strategy;

    }

    public double calculatePremium(double basePremium, Map<String, Object> userData) {

        return strategy.calculatePremium(basePremium, userData);

    }

}