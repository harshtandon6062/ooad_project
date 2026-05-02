package com.example.insureflow.patterns.behavioral;

import java.util.Map;

public class StandardPremiumStrategy implements PremiumStrategy {

    @Override
    public double calculatePremium(double basePremium, Map<String, Object> userData) {

        int age = (int) userData.get("age");

        double factor = 1.0;

        if (age < 25) factor = 1.5;

        else if (age >= 25 && age <= 50) factor = 1.0;

        else if (age > 50 && age <= 65) factor = 1.3;

        else if (age > 65) factor = 1.6;

        return basePremium * factor;

    }

}