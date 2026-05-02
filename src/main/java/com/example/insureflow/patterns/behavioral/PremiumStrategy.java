package com.example.insureflow.patterns.behavioral;

import java.math.BigDecimal;
import java.util.Map;

public interface PremiumStrategy {
    BigDecimal calculate(BigDecimal basePremium, Map<String, Object> userData);
}
