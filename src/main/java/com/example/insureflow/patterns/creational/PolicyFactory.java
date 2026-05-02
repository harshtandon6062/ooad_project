package com.example.insureflow.patterns.creational;

import com.example.insureflow.model.Policy;
import com.example.insureflow.model.PolicyStatus;

import java.time.LocalDate;

public class PolicyFactory {

    public static Policy createTemplate(String type, String provider, double premium, double coverageAmount) {

        return new Policy(type, provider, premium, coverageAmount, null, PolicyStatus.ACTIVE, null, null);

    }

    public static Policy issuePolicy(String type, String provider, double premium, double coverageAmount, LocalDate startDate, LocalDate expiryDate) {

        return new Policy(type, provider, premium, coverageAmount, null, PolicyStatus.ACTIVE, startDate, expiryDate);

    }

    // Additional methods would require repository access

}