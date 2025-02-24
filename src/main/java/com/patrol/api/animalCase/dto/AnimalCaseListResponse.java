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
    CaseStatus caseStatus
) {

  public static AnimalCaseListResponse of(AnimalCase animalCase) {
    return new AnimalCaseListResponse(
        animalCase.getAnimal().getName(),
        animalCase.getCreatedAt(),
        animalCase.getModifiedAt(),
        animalCase.getStatus()
    );
  }
}

