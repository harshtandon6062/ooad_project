package com.example.insureflow.patterns.behavioral;

public class ExpiredPolicyStateImpl implements PolicyState {
    @Override
    public void activate(PolicyContext context) {
        context.setState(new ActivePolicyStateImpl());
    }

    @Override
    public void expire(PolicyContext context) { }

    @Override
    public void cancel(PolicyContext context) {
        context.setState(new CancelledPolicyStateImpl());
    }

    @Override
    public void suspend(PolicyContext context) { }

    @Override
    public String getStatusString() {
        return "expired";
    }
}
