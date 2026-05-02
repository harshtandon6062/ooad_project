package com.example.insureflow.patterns.behavioral;

public class CancelledPolicyState implements PolicyState {

    @Override
    public void renew(PolicyContext context) {

        // No action

    }

    @Override
    public void cancel(PolicyContext context) {

        // Already cancelled

    }

    @Override
    public void suspend(PolicyContext context) {

        // No action

    }

    @Override
    public void fileClaim(PolicyContext context) {

        // Cannot file claim

    }

}