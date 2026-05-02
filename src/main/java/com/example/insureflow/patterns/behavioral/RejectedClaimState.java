package com.example.insureflow.patterns.behavioral;

public class RejectedClaimState implements ClaimState {

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

        // Already rejected

    }

    @Override
    public void payout(ClaimContext context) {

        // No action

    }

}