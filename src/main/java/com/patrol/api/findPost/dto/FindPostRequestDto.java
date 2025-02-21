package com.patrol.api.findPost.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FindPostRequestDto {
    private String title;
    private String content;
    private List<String> tags;
    private Double latitude;  // 위도 추가
    private Double longitude; // 경도 추가
    private String location;
    private String findTime;
    //private Long lostId;
    private String status; // 상태를 String으로 받음
    private LocalDate birthDate;  // 출생일
    private String breed;  // 품종
    private String name;  // 이름
    private String characteristics;  // 특징
    private String size;  // SMALL, MEDIUM, LARGE
    private String gender;  // MALE, FEMALE
}

