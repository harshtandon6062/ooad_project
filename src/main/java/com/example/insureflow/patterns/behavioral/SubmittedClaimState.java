package com.example.insureflow.patterns.behavioral;

public class SubmittedClaimState implements ClaimState {

    @Override
    public void startReview(ClaimContext context) {

        context.setState(new UnderReviewClaimState());

        System.out.println("Claim review started");

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

        // No action

    }

}