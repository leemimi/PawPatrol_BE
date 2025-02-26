package com.patrol.api.animalCase.dto;


import com.patrol.domain.animalCase.entity.AnimalCase;
import com.patrol.domain.animalCase.enums.CaseStatus;
import lombok.Builder;

import java.time.LocalDateTime;


@Builder
public record AnimalCaseListResponse(
    String animalName,
    LocalDateTime createdAt,
    LocalDateTime modifiedAt,
    CaseStatus caseStatus,
    String imageUrl
) {

  public static AnimalCaseListResponse of(AnimalCase animalCase) {
    return AnimalCaseListResponse.builder()
        .animalName(animalCase.getAnimal().getName())
        .createdAt(animalCase.getCreatedAt())
        .modifiedAt(animalCase.getModifiedAt())
        .caseStatus(animalCase.getStatus())
        .imageUrl(animalCase.getAnimal().getImageUrl())
        .build();
  }
}

