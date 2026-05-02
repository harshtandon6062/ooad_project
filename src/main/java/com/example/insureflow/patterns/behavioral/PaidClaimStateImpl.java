package com.example.insureflow.patterns.behavioral;

public class PaidClaimStateImpl implements ClaimState {
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
    public String getStatusString() { return "paid"; }
}
