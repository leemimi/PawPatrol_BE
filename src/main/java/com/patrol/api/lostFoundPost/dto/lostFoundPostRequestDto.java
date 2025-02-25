package com.patrol.api.lostFoundPost.dto;

import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class lostFoundPostRequestDto {
    private String content;
    private Double latitude;  // 위도 추가
    private Double longitude; // 경도 추가
    private String location;
    private String findTime;
    private String lostTime;
    private String status; // 상태를 String으로 받음
}

