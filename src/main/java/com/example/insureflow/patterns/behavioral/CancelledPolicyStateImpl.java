package com.example.insureflow.patterns.behavioral;

public class CancelledPolicyStateImpl implements PolicyState {
    @Override
    public void activate(PolicyContext context) { }
    @Override
    public void expire(PolicyContext context) { }
    @Override
    public void cancel(PolicyContext context) { }
    @Override
    public void suspend(PolicyContext context) { }
    @Override
    public String getStatusString() {
        return "cancelled";
    }
}
