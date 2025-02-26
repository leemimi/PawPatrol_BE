package com.patrol.domain.lostFoundPost.entity;

public enum AnimalType {
    DOG("강아지"),
    CAT("고양이");

    private final String description;

    AnimalType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

