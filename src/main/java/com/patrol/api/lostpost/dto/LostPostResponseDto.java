package com.patrol.api.lostpost.dto;

import com.patrol.domain.lostPost.entity.LostPost;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LostPostResponseDto {
    private Long lostId;
    private String content;     // 내용
    private Double longitude;   // 경도
    private Double latitude;    // 위도
    private String location;    // 위치
    private String lostTime;    // 실종 시간

    public LostPostResponseDto(LostPost lostPost) {
        this.lostId = lostPost.getId();
        this.content = lostPost.getContent();
        this.longitude = lostPost.getLongitude();
        this.latitude = lostPost.getLatitude();
        this.location = lostPost.getLocation();
        this.lostTime = lostPost.getLostTime().toString();  // LocalDateTime을 String으로 변환
    }

    public static LostPostResponseDto from(LostPost lostPost) {
        return new LostPostResponseDto(lostPost);
    }
}

