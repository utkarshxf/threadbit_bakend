package com.backend.threadbit.model;

public enum Size {
    XS, S, M, L, XL, XXL,
    SIZE_6("6"), SIZE_7("7"), SIZE_8("8"), SIZE_9("9"),
    SIZE_10("10"), SIZE_11("11"), SIZE_12("12");

    private final String value;

    Size() {
        this.value = name();
    }

    Size(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
