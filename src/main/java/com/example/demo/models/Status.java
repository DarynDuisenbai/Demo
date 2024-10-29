package com.example.demo.models;

import lombok.Getter;

@Getter
public enum Status {
    UNDER_APPROVAL("Under Approval"),
    APPROVED("Approved"),
    REFUSED("Refused"),
    LEFT_WITHOUT_CONSIDERATION("Left Without Consideration"),
    SENT_FOR_REVISION("Sent For Revision");

    private final String label;

    Status(String label) {
        this.label = label;
    }
}

