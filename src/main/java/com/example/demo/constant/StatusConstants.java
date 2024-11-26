package com.example.demo.constant;

public enum StatusConstants {
    AGREED("Согласовано"),
    REFUSED("Отказано"),
    TO_BE_AGREED("На согласовании"),
    LEFT_WITHOUT_CONSIDERATION("Оставлено без рассмотрения"),
    IN_PROGRESS("В работе");

    private final String label;

    StatusConstants(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
