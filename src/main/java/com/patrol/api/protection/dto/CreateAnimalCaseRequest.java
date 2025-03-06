package com.patrol.api.protection.dto;

import com.patrol.domain.animal.entity.Animal;
import com.patrol.domain.animal.enums.AnimalGender;
import com.patrol.domain.animal.enums.AnimalSize;
import com.patrol.domain.animal.enums.AnimalType;

public record CreateAnimalCaseRequest(
    String title,             // 게시글 제목
    String description,       // 게시글 상세 설명
    String location,          // 지역
    String breed,              // 품종
    AnimalGender gender,       // 성별
    AnimalSize size,          // 크기
    String feature,           // 특징
    String healthCondition,   // 건강상태
    String name,              // 이름
    String estimatedAge,      // 추정 나이
    String registrationNo,    // 동물등록번호
    AnimalType animalType     // 동물 종류 (강아지/고양이)
) {
  public Animal toAnimal() {
    return Animal.builder()
        .breed(breed)
        .gender(gender)
        .size(size)
        .feature(feature)
        .healthCondition(healthCondition)
        .name(name)
        .estimatedAge(estimatedAge)
        .registrationNo(registrationNo)
        .animalType(animalType)
        .build();
  }
}
