package com.example.insureflow.patterns.behavioral;

import java.time.LocalDate;

public interface PolicyState {
    void activate(PolicyContext context);
    void expire(PolicyContext context);
    void cancel(PolicyContext context);
    void suspend(PolicyContext context);
    String getStatusString();
}
