package com.example.insureflow.patterns.behavioral;

public interface ClaimState {
    void submit(ClaimContext context);
    void review(ClaimContext context);
    void approve(ClaimContext context);
    void reject(ClaimContext context);
    void pay(ClaimContext context);
    String getStatusString();
}
