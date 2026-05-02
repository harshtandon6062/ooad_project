package com.example.insureflow.patterns.behavioral;

public class UnderReviewClaimState implements ClaimState {

    @Override
    public void startReview(ClaimContext context) {

        // Already under review

    }

    @Override
    public void approve(ClaimContext context) {

        context.setState(new ApprovedClaimState());

        System.out.println("Claim approved");

    }

    @Override
    public void reject(ClaimContext context) {

        context.setState(new RejectedClaimState());

        System.out.println("Claim rejected");

    }

    @Override
    public void payout(ClaimContext context) {

        // No action

    }

}