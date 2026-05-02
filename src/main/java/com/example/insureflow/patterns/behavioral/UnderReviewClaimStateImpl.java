package com.example.insureflow.patterns.behavioral;

public class UnderReviewClaimStateImpl implements ClaimState {
    @Override
    public void submit(ClaimContext context) { }
    @Override
    public void review(ClaimContext context) { }
    @Override
    public void approve(ClaimContext context) { context.setState(new ApprovedClaimStateImpl()); }
    @Override
    public void reject(ClaimContext context) { context.setState(new RejectedClaimStateImpl()); }
    @Override
    public void pay(ClaimContext context) { }
    @Override
    public String getStatusString() { return "under_review"; }
}
