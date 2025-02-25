package com.patrol.api.animalCase.dto;

import com.patrol.domain.animalCase.entity.AnimalCase;
import com.patrol.domain.animalCase.enums.CaseStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;


@Builder
public record AnimalCaseDetailResponse(
    String animalName,
    LocalDateTime createdAt, LocalDateTime modifiedAt,
    CaseStatus caseStatus, List<CaseHistoryResponse> caseHistoryList
) {

  public static AnimalCaseDetailResponse of(AnimalCase animalCase) {
    List<CaseHistoryResponse> caseHistoryResponseList =
        animalCase.getHistories().stream()
            .map(CaseHistoryResponse::of)
            .toList();

    return AnimalCaseDetailResponse.builder()
        .animalName(animalCase.getAnimal().getName())
        .createdAt(animalCase.getCreatedAt())
        .modifiedAt(animalCase.getModifiedAt())
        .caseStatus(animalCase.getStatus())
        .caseHistoryList(caseHistoryResponseList)
        .build();
  }
}
