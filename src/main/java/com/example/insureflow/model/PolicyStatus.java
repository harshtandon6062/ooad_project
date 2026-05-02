package com.example.insureflow.model;

public enum PolicyStatus {
    ACTIVE("active"),
    EXPIRED("expired"),
    CANCELLED("cancelled"),
    SUSPENDED("suspended"),
    TEMPLATE("template");

    private final String label;

    PolicyStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static PolicyStatus fromLabel(String label) {
        for (PolicyStatus status : PolicyStatus.values()) {
            if (status.label.equalsIgnoreCase(label)) {
                return status;
            }
        }
        return TEMPLATE;
    }
}
