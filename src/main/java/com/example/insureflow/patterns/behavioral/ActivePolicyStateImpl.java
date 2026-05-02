package com.example.insureflow.patterns.behavioral;

public class ActivePolicyStateImpl implements PolicyState {
    @Override
    public void activate(PolicyContext context) { }

    @Override
    public void expire(PolicyContext context) {
        context.setState(new ExpiredPolicyStateImpl());
    }

    @Override
    public void cancel(PolicyContext context) {
        context.setState(new CancelledPolicyStateImpl());
    }

    @Override
    public void suspend(PolicyContext context) {
        context.setState(new SuspendedPolicyStateImpl());
    }

    @Override
    public String getStatusString() {
        return "active";
    }
}
