package com.patrol.api.LostPost.dto;

import com.patrol.domain.lostPost.entity.LostPost;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LostPostResponseDto {
    private Long lostId;
    private String status;
    private String title;       // 제목
    private String content;     // 내용
    private String tags;  // 태그를 String으로 저장 (구분자로 연결된 문자열)
    private Double longitude;   // 경도
    private Double latitude;    // 위도
    private String location;    // 위치
    private String ownerPhone;  // 보호자 전화번호
    private String lostTime;    // 실종 시간

    public LostPostResponseDto(LostPost lostPost) {
        this.lostId = lostPost.getId();
        // status가 Enum일 때 getDescription()을 사용하여 String 값을 가져옴
        this.status = lostPost.getStatus().getDescription();
        this.title = lostPost.getTitle();
        this.content = lostPost.getContent();
        this.tags = lostPost.getTags();
        this.longitude = lostPost.getLongitude();
        this.latitude = lostPost.getLatitude();
        this.location = lostPost.getLocation();
        this.ownerPhone = lostPost.getOwnerPhone();
        this.lostTime = lostPost.getLostTime().toString();  // LocalDateTime을 String으로 변환
    }

    public static LostPostResponseDto from(LostPost lostPost) {
        return new LostPostResponseDto(lostPost);
    }
}
