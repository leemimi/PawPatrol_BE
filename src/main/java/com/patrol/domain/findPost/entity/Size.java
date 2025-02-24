package com.patrol.domain.findPost.entity;

public enum Size {
    SMALL("소형"),
    MEDIUM("중형"),
    LARGE("대형");

    private final String description;

    Size(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
