package com.example.insureflow.patterns.behavioral;

public interface ClaimState {

    void startReview(ClaimContext context);

    void approve(ClaimContext context);

    void reject(ClaimContext context);

    void payout(ClaimContext context);

}