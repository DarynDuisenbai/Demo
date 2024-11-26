package com.example.demo.constant;


public enum JobConstants {
    EMPLOYEE("Сотрудник СУ"),
    CURATOR("Куратор отдела"),
    SPECIALIST("Специалист по данным"),
    ANALYST("Аналитик СД"),
    MODERATOR("Модератор");

    private final String label;

    JobConstants(String label) {
        this.label = label;
    }
    public String getLabel() {
        return label;
    }
}
