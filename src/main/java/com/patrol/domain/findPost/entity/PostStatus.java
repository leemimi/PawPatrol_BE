package com.patrol.domain.findPost.entity;

public enum PostStatus {
    FINDING("찾는 중"),
    FOSTERING("임보 중"),
    FOUND("주인 찾기 완료");

    private final String description;

    PostStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
