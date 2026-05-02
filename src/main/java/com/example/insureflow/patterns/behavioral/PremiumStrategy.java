package com.example.insureflow.patterns.behavioral;

import java.util.Map;

public interface PremiumStrategy {

    double calculatePremium(double basePremium, Map<String, Object> userData);

}