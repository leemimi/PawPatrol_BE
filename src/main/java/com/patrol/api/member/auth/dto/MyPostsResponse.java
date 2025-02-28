package com.patrol.api.member.auth.dto;

import com.patrol.domain.lostFoundPost.entity.PostStatus;

import java.time.LocalDateTime;

/**
 * packageName    : com.patrol.api.member.auth.dto
 * fileName       : MyPostsResponse
 * author         : sungjun
 * date           : 2025-02-27
 * description    : 자동 주석 생성
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-27        kyd54       최초 생성
 */
public record MyPostsResponse (
        String content,
        PostStatus status,
        String findTime,
        String lostTime,
        String createPostTime
){}
