package com.example.insureflow.patterns.behavioral;

public class ExpiredPolicyState implements PolicyState {

    @Override
    public void renew(PolicyContext context) {

        context.setState(new ActivePolicyState());

        System.out.println("Policy renewed from expired");

    }

    @Override
    public void cancel(PolicyContext context) {

        // No action

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