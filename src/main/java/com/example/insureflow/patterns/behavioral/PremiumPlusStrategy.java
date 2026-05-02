package com.example.insureflow.patterns.behavioral;

import java.math.BigDecimal;
import java.util.Map;

public class PremiumPlusStrategy implements PremiumStrategy {
    @Override
    public BigDecimal calculate(BigDecimal basePremium, Map<String, Object> userData) {
        BigDecimal premium = basePremium;
        premium = premium.multiply(getAgeFactor(userData));
        premium = premium.multiply(getLifestyleFactor(userData));
        premium = premium.multiply(getOccupationFactor(userData));
        return premium.setScale(2, java.math.RoundingMode.HALF_UP);
    }
    
    private BigDecimal getAgeFactor(Map<String, Object> userData) {
        if (!userData.containsKey("age")) return BigDecimal.ONE;
        Integer age = (Integer) userData.get("age");
        if (age < 25) return BigDecimal.valueOf(1.5);
        if (age < 40) return BigDecimal.valueOf(1.0);
        if (age < 60) return BigDecimal.valueOf(1.4);
        return BigDecimal.valueOf(1.8);
    }
    
    private BigDecimal getLifestyleFactor(Map<String, Object> userData) {
        if (!userData.containsKey("lifestyle")) return BigDecimal.ONE;
        String lifestyle = (String) userData.get("lifestyle");
        return switch(lifestyle) {
            case "smoker" -> BigDecimal.valueOf(1.4);
            case "drinker" -> BigDecimal.valueOf(1.2);
            default -> BigDecimal.ONE;
        };
    }
    
    private BigDecimal getOccupationFactor(Map<String, Object> userData) {
        if (!userData.containsKey("occupation")) return BigDecimal.ONE;
        String occupation = (String) userData.get("occupation");
        return switch(occupation) {
            case "driver" -> BigDecimal.valueOf(1.5);
            case "construction" -> BigDecimal.valueOf(1.3);
            case "office" -> BigDecimal.valueOf(1.0);
            default -> BigDecimal.valueOf(1.1);
        };
    }
}
