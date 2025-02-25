package com.patrol.api.animal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * packageName    : com.patrol.api.animal.dto
 * fileName       : MyPetListResponse
 * author         : sungjun
 * date           : 2025-02-24
 * description    : 자동 주석 생성
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-24        kyd54       최초 생성
 */
@Getter
@Builder
@AllArgsConstructor
public class MyPetListResponse {
    private Long id;
    private String name;
    private String breed;
    private String estimatedAge;
    private String feature;
    private String size;
    private String registrationNumber;
    private String imageUrl;
    private String healthCondition;
}
