package com.patrol.domain.lostFoundPost.entity;


public enum PostStatus {
    SHELTER("보호소"),
    FOSTERING("임보 중"),
    FOUND("주인 찾기 완료"),
    FINDING("실종"),
    SIGHTED("목격"); // 올바른 위치로 변경된 부분

    private final String description;

    PostStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    // String 값을 PostStatus로 변환하는 메서드 추가
    public static PostStatus fromString(String status) {
        for (PostStatus ps : PostStatus.values()) {
            if (ps.name().equalsIgnoreCase(status)) { // 대소문자 구분하지 않고 비교
                return ps;
            }
        }
        throw new IllegalArgumentException("Invalid status value: " + status);
    }






}