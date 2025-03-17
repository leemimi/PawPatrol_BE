package com.patrol.api.protection.dto;

import com.patrol.domain.animalCase.entity.AnimalCase;
import com.patrol.domain.animalCase.enums.CaseStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record MyAnimalCaseResponse(
    Long animalCaseId,
    String title,
    String animalName,
    String imageUrl,
    CaseStatus caseStatus,
    LocalDateTime createdAt,
    List<PendingProtectionResponse> pendingProtections,
    int pendingApplicationsCount
) {

  public static MyAnimalCaseResponse of(
      AnimalCase animalCase, int pendingCount, List<PendingProtectionResponse> pendingProtections
  ) {
    return MyAnimalCaseResponse.builder()
        .animalCaseId(animalCase.getId())
        .title(animalCase.getTitle())
        .animalName(animalCase.getAnimal().getName())
        .imageUrl(animalCase.getAnimal().getImageUrl())
        .caseStatus(animalCase.getStatus())
        .createdAt(animalCase.getCreatedAt())
        .pendingApplicationsCount(pendingCount)
        .pendingProtections(pendingProtections)
        .build();
  }
}
