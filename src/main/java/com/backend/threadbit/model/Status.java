package com.backend.threadbit.model;
public enum Status {
    ACTIVE("active"),
    ENDED("ended"),
    COMPLETED("completed"),
    CANCELLED("cancelled");

    private final String value;

    Status(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}