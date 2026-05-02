package com.example.insureflow.model;

public enum UserRole {
    CUSTOMER("customer"),
    AGENT("agent"),
    ADMIN("admin"),
    CLAIMS_ADJUSTER("claims_adjuster");

    private final String label;

    UserRole(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static UserRole fromLabel(String label) {
        for (UserRole role : UserRole.values()) {
            if (role.label.equalsIgnoreCase(label)) {
                return role;
            }
        }
        return CUSTOMER;
    }
}
