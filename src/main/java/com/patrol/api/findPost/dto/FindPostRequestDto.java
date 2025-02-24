package com.patrol.api.findPost.dto;

import io.swagger.v3.oas.annotations.info.Info;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindPostRequestDto {
    private String content;
    private Double latitude;  // 위도 추가
    private Double longitude; // 경도 추가
    private String location;
    private String findTime;
    private String status; // 상태를 String으로 받음
}

