package com.patrol.domain.lostFoundPost.entity;

public enum PostStatus {
    SHELTER("보호소"),
    FOSTERING("임보 중"),
    FOUND("주인 찾기 완료"),
    FINDING("실종");

    private final String description;

    PostStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
