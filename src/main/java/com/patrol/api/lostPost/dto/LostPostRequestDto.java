package com.patrol.api.lostPost.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LostPostRequestDto {
    private String content;
    private String location;
    private Double latitude;
    private Double longitude;
    private LocalDateTime lostTime;
    private String status;
}
