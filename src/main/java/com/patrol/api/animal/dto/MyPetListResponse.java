package com.patrol.api.animal.dto;

import com.patrol.domain.animal.enums.AnimalGender;
import com.patrol.domain.animal.enums.AnimalSize;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    @Enumerated(EnumType.STRING)
    private AnimalSize size;
    private String registrationNo;
    private String imageUrl;
    private String healthCondition;
    @Enumerated(EnumType.STRING)
    private AnimalGender gender;
}
