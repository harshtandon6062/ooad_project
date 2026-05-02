package com.example.insureflow.patterns.behavioral;

import com.example.insureflow.model.Claim;
import com.example.insureflow.model.ClaimStatus;
import java.time.LocalDateTime;

public class ClaimContext {
    private Claim claim;
    private ClaimState state;

    public ClaimContext(Claim claim) {
        this.claim = claim;
        this.state = new SubmittedClaimStateImpl();
    }

    public void submit() {
        state.submit(this);
    }

    public void review() {
        state.review(this);
        setState(new UnderReviewClaimStateImpl());
    }

    public void approve() {
        state.approve(this);
        setState(new ApprovedClaimStateImpl());
        claim.setStatus(ClaimStatus.APPROVED);
    }

    public void reject() {
        state.reject(this);
        setState(new RejectedClaimStateImpl());
        claim.setStatus(ClaimStatus.REJECTED);
        claim.setResolvedDate(LocalDateTime.now());
    }

    public void pay() {
        state.pay(this);
        setState(new PaidClaimStateImpl());
        claim.setStatus(ClaimStatus.PAID);
        claim.setResolvedDate(LocalDateTime.now());
    }

    public Claim getClaim() {
        return claim;
    }

    public void setState(ClaimState state) {
        this.state = state;
    }

    public ClaimState getState() {
        return state;
    }
}
