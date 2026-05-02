package com.example.insureflow.patterns.behavioral;

public class ActivePolicyState implements PolicyState {

    @Override
    public void renew(PolicyContext context) {

        System.out.println("Policy renewed");

    }

    @Override
    public void cancel(PolicyContext context) {

        context.setState(new CancelledPolicyState());

        System.out.println("Policy cancelled");

    }

    @Override
    public void suspend(PolicyContext context) {

        context.setState(new SuspendedPolicyState());

        System.out.println("Policy suspended");

    }

    @Override
    public void fileClaim(PolicyContext context) {

        System.out.println("Claim filed");

    }

}