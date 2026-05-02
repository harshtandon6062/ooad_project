package com.example.insureflow.patterns.behavioral;

public class PaidClaimState implements ClaimState {

    @Override
    public void startReview(ClaimContext context) {

        // No action

    }

    @Override
    public void approve(ClaimContext context) {

        // No action

    }

    @Override
    public void reject(ClaimContext context) {

        // No action

    }

    @Override
    public void payout(ClaimContext context) {

        // Already paid

    }

}