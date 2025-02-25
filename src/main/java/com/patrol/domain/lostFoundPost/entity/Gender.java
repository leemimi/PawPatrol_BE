package com.patrol.domain.lostFoundPost.entity;


public enum Gender {
    MALE("남자"),
    FEMALE("여자");

    private final String description;

    Gender(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}