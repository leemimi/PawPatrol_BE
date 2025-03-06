package com.patrol.api.animalCase.dto;

import com.patrol.domain.animalCase.entity.AnimalCase;
import com.patrol.domain.animalCase.enums.CaseStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;


@Builder
public record AnimalCaseDetailDto(
    AnimalInfo animalInfo, String currentFosterName, String title,
    String description, String location, LocalDateTime createdAt, LocalDateTime modifiedAt,
    CaseStatus caseStatus, List<CaseHistoryResponse> caseHistoryList
) {

  public static AnimalCaseDetailDto of(AnimalCase animalCase) {
    List<CaseHistoryResponse> caseHistoryResponseList =
        animalCase.getHistories().stream()
            .map(CaseHistoryResponse::of)
            .toList();

    return AnimalCaseDetailDto.builder()
        .animalInfo(AnimalInfo.of(animalCase.getAnimal()))
        .currentFosterName(animalCase.getCurrentFoster().getNickname())
        .title(animalCase.getTitle())
        .description(animalCase.getDescription())
        .location(animalCase.getLocation())
        .createdAt(animalCase.getCreatedAt())
        .modifiedAt(animalCase.getModifiedAt())
        .caseStatus(animalCase.getStatus())
        .caseHistoryList(caseHistoryResponseList)
        .build();
  }
}
