package com.example.insureflow.patterns.behavioral;

public class ApprovedClaimState implements ClaimState {

    @Override
    public void startReview(ClaimContext context) {

        // No action

    }

    @Override
    public void approve(ClaimContext context) {

        // Already approved

    }

    @Override
    public void reject(ClaimContext context) {

        // No action

    }

    @Override
    public void payout(ClaimContext context) {

        context.setState(new PaidClaimState());

        System.out.println("Claim paid");

    }

}