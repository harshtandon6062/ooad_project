package com.example.insureflow.patterns.behavioral;

public class ClaimContext {

    private ClaimState state;

    private String claimId;

    public ClaimContext(String claimId, String initialState) {

        this.claimId = claimId;

        switch (initialState) {

            case "submitted": state = new SubmittedClaimState(); break;

            case "under_review": state = new UnderReviewClaimState(); break;

            case "approved": state = new ApprovedClaimState(); break;

            case "rejected": state = new RejectedClaimState(); break;

            case "paid": state = new PaidClaimState(); break;

            default: state = new SubmittedClaimState(); break;

        }

    }

    public void setState(ClaimState state) {

        this.state = state;

    }

    public void startReview() { state.startReview(this); }

    public void approve() { state.approve(this); }

    public void reject() { state.reject(this); }

    public void payout() { state.payout(this); }

}