package com.example.insureflow.patterns.behavioral;

public class RejectedClaimStateImpl implements ClaimState {
    @Override
    public void submit(ClaimContext context) { }
    @Override
    public void review(ClaimContext context) { }
    @Override
    public void approve(ClaimContext context) { }
    @Override
    public void reject(ClaimContext context) { }
    @Override
    public void pay(ClaimContext context) { }
    @Override
    public String getStatusString() { return "rejected"; }
}
