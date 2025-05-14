package com.backend.threadbit.model;

public enum Condition {
    NEW_WITH_TAGS("New with tags"),
    LIKE_NEW("Like new"),
    GENTLY_USED("Gently used"),
    WELL_WORN("Well worn");

    private final String value;

    Condition(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
