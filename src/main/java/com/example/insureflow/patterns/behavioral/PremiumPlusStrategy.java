package com.example.insureflow.patterns.behavioral;

import java.util.Map;

public class PremiumPlusStrategy implements PremiumStrategy {

    @Override
    public double calculatePremium(double basePremium, Map<String, Object> userData) {

        int age = (int) userData.get("age");

        double factor = 1.0;

        if (age < 25) factor = 1.5;

        else if (age >= 25 && age <= 50) factor = 1.0;

        else if (age > 50 && age <= 65) factor = 1.3;

        else if (age > 65) factor = 1.6;

        // Lifestyle factors

        boolean smoker = (boolean) userData.getOrDefault("smoker", false);

        if (smoker) factor += 0.2;

        String occupation = (String) userData.getOrDefault("occupation", "low");

        if ("construction".equals(occupation) || "mining".equals(occupation)) factor += 0.3;

        return basePremium * factor;

    }

}