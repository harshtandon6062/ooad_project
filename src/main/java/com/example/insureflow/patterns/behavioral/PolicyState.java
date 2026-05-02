package com.example.insureflow.patterns.behavioral;

public interface PolicyState {

    void renew(PolicyContext context);

    void cancel(PolicyContext context);

    void suspend(PolicyContext context);

    void fileClaim(PolicyContext context);

}