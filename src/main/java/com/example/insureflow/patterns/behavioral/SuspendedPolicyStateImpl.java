package com.example.insureflow.patterns.behavioral;

public class SuspendedPolicyStateImpl implements PolicyState {
    @Override
    public void activate(PolicyContext context) {
        context.setState(new ActivePolicyStateImpl());
    }

    @Override
    public void expire(PolicyContext context) {
        context.setState(new ExpiredPolicyStateImpl());
    }

    @Override
    public void cancel(PolicyContext context) {
        context.setState(new CancelledPolicyStateImpl());
    }

    @Override
    public void suspend(PolicyContext context) { }

    @Override
    public String getStatusString() {
        return "suspended";
    }
}
