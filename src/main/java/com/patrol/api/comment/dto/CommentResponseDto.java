package com.patrol.api.comment.dto;


import com.patrol.domain.comment.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
    private Long id;
    private String content;
    private String nickname;
    private Long lostFoundPostId;
    private Long userId;
    private LocalDateTime createdAt;
    private String profileImage;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.userId = comment.getAuthor().getId();
        this.content = comment.getContent();
        this.nickname = comment.getAuthor().getNickname();
        this.lostFoundPostId = comment.getLostFoundPost() != null ? comment.getLostFoundPost().getId() : null;
        this.createdAt = comment.getCreatedAt();
        this.profileImage = comment.getAuthor().getProfileImageUrl();
    }
}
