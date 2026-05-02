package com.example.insureflow.patterns.behavioral;

import java.math.BigDecimal;
import java.util.Map;

public class StandardPremiumStrategy implements PremiumStrategy {
    @Override
    public BigDecimal calculate(BigDecimal basePremium, Map<String, Object> userData) {
        BigDecimal premium = basePremium;
        
        if (userData.containsKey("age")) {
            Integer age = (Integer) userData.get("age");
            if (age < 25) {
                premium = premium.multiply(BigDecimal.valueOf(1.5));
            } else if (age >= 25 && age < 50) {
                premium = premium.multiply(BigDecimal.valueOf(1.0));
            } else if (age >= 50 && age < 65) {
                premium = premium.multiply(BigDecimal.valueOf(1.3));
            } else {
                premium = premium.multiply(BigDecimal.valueOf(1.6));
            }
        }
        
        return premium.setScale(2, java.math.RoundingMode.HALF_UP);
    }
}
