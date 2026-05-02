package com.example.insureflow.patterns.behavioral;

import java.math.BigDecimal;
import java.util.Map;

public class DiscountedPremiumStrategyImpl implements PremiumStrategy {
    @Override
    public BigDecimal calculate(BigDecimal basePremium, Map<String, Object> userData) {
        BigDecimal premium = basePremium;
        if (userData.containsKey("discountPercent")) {
            Integer discountPercent = (Integer) userData.get("discountPercent");
            BigDecimal discountFactor = BigDecimal.ONE.subtract(
                BigDecimal.valueOf(discountPercent).divide(BigDecimal.valueOf(100))
            );
            premium = premium.multiply(discountFactor);
        }
        return premium.setScale(2, java.math.RoundingMode.HALF_UP);
    }
}
