package com.example.insureflow.patterns.behavioral;

public class ApprovedClaimStateImpl implements ClaimState {
    @Override
    public void submit(ClaimContext context) { }
    @Override
    public void review(ClaimContext context) { }
    @Override
    public void approve(ClaimContext context) { }
    @Override
    public void reject(ClaimContext context) { }
    @Override
    public void pay(ClaimContext context) { context.setState(new PaidClaimStateImpl()); }
    @Override
    public String getStatusString() { return "approved"; }
}
