package com.example.insureflow.patterns.behavioral;

public class SuspendedPolicyState implements PolicyState {

    @Override
    public void renew(PolicyContext context) {

        // No action

    }

    @Override
    public void cancel(PolicyContext context) {

        context.setState(new CancelledPolicyState());

        System.out.println("Policy cancelled from suspended");

    }

    @Override
    public void suspend(PolicyContext context) {

        // Already suspended

    }

    @Override
    public void fileClaim(PolicyContext context) {

        // Cannot file claim

    }

    public void reactivate(PolicyContext context) {

        context.setState(new ActivePolicyState());

        System.out.println("Policy reactivated");

    }

}