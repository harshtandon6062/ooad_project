package com.example.insureflow.patterns.behavioral;

public class PolicyContext {

    private PolicyState state;

    private String policyId;

    public PolicyContext(String policyId, String initialState) {

        this.policyId = policyId;

        switch (initialState) {

            case "active": state = new ActivePolicyState(); break;

            case "expired": state = new ExpiredPolicyState(); break;

            case "cancelled": state = new CancelledPolicyState(); break;

            case "suspended": state = new SuspendedPolicyState(); break;

            default: state = new ActivePolicyState(); break;

        }

    }

    public void setState(PolicyState state) {

        this.state = state;

    }

    public void renew() { state.renew(this); }

    public void cancel() { state.cancel(this); }

    public void suspend() { state.suspend(this); }

    public void fileClaim() { state.fileClaim(this); }

}