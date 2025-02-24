package com.patrol.api.LostPost.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LostPostRequestDto {
    private String content;
    private String location;
    private Double latitude;
    private Double longitude;
    private String lostTime;
    private String status;
}
