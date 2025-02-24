package com.patrol.api.findPost.dto;

import com.patrol.api.member.member.dto.MemberResponseDto;
import com.patrol.domain.findPost.entity.FindPost;
import com.patrol.domain.member.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FindPostResponseDto {
    private Long foundId;
    private MemberResponseDto author;
    private String content;
    private Double latitude;
    private Double longitude;
    private String findTime;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String status;

    // 엔티티에서 DTO 변환 생성자
    public FindPostResponseDto(FindPost findPost) {
        this.foundId = findPost.getId();
        this.author = new MemberResponseDto(findPost.getAuthor());
        this.content = findPost.getContent();
        this.latitude = findPost.getLatitude();
        this.longitude = findPost.getLongitude();
        this.findTime = findPost.getFindTime();
        this.status= String.valueOf(findPost.getStatus());
        this.createdAt = findPost.getCreatedAt();
        this.modifiedAt = findPost.getModifiedAt();
    }

    public static FindPostResponseDto from(FindPost findPost) {
        return new FindPostResponseDto(findPost);
    }
}

