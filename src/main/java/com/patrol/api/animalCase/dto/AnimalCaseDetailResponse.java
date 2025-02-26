package com.patrol.api.animalCase.dto;

import com.patrol.domain.animalCase.entity.AnimalCase;
import com.patrol.domain.animalCase.enums.CaseStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;


@Builder
public record AnimalCaseDetailResponse(
    AnimalInfo animalInfo, String currentFosterName,
    LocalDateTime createdAt, LocalDateTime modifiedAt,
    CaseStatus caseStatus, List<CaseHistoryResponse> caseHistoryList
) {

  public static AnimalCaseDetailResponse of(AnimalCase animalCase) {
    List<CaseHistoryResponse> caseHistoryResponseList =
        animalCase.getHistories().stream()
            .map(CaseHistoryResponse::of)
            .toList();

    return AnimalCaseDetailResponse.builder()
        .animalInfo(AnimalInfo.of(animalCase.getAnimal()))
        .currentFosterName(animalCase.getCurrentFoster().getNickname())
        .createdAt(animalCase.getCreatedAt())
        .modifiedAt(animalCase.getModifiedAt())
        .caseStatus(animalCase.getStatus())
        .caseHistoryList(caseHistoryResponseList)
        .build();
  }
}
