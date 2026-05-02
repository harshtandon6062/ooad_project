package com.example.insureflow.patterns.behavioral;

public class SubmittedClaimStateImpl implements ClaimState {
    @Override
    public void submit(ClaimContext context) { }
    @Override
    public void review(ClaimContext context) { context.setState(new UnderReviewClaimStateImpl()); }
    @Override
    public void approve(ClaimContext context) { }
    @Override
    public void reject(ClaimContext context) { }
    @Override
    public void pay(ClaimContext context) { }
    @Override
    public String getStatusString() { return "submitted"; }
}
