package com.example.insureflow.patterns.creational;

import com.example.insureflow.model.Claim;
import com.example.insureflow.model.ClaimStatus;

import java.time.LocalDate;

public class ClaimFactory {

    public static Claim submitClaim(double amount, String description) {

        return new Claim(null, null, amount, description, ClaimStatus.SUBMITTED, LocalDate.now());

    }

    // Additional methods would require repository access

}