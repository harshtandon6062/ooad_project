package com.example.insureflow.model;

public enum ClaimStatus {
    SUBMITTED("submitted"),
    UNDER_REVIEW("under_review"),
    APPROVED("approved"),
    REJECTED("rejected"),
    PAID("paid");

    private final String label;

    ClaimStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static ClaimStatus fromLabel(String label) {
        for (ClaimStatus status : ClaimStatus.values()) {
            if (status.label.equalsIgnoreCase(label)) {
                return status;
            }
        }
        return SUBMITTED;
    }
}
