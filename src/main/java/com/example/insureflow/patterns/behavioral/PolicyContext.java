package com.example.insureflow.patterns.behavioral;

import java.time.LocalDate;

public class PolicyContext {
    private PolicyState state;
    private Long policyId;
    private LocalDate expiryDate;

    public PolicyContext(Long policyId, LocalDate expiryDate) {
        this.policyId = policyId;
        this.expiryDate = expiryDate;
        this.state = new ActivePolicyStateImpl();
    }

    public void setState(PolicyState state) {
        this.state = state;
    }

    public PolicyState getState() {
        return state;
    }

    public void activate() { state.activate(this); }
    public void expire() { state.expire(this); }
    public void cancel() { state.cancel(this); }
    public void suspend() { state.suspend(this); }
    public String getStatus() { return state.getStatusString(); }
    public Long getPolicyId() { return policyId; }
    public LocalDate getExpiryDate() { return expiryDate; }
}
