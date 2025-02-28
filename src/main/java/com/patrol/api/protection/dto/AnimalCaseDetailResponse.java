package com.patrol.api.protection.dto;

import com.patrol.api.animalCase.dto.AnimalCaseDetailDto;
import java.util.List;

public record AnimalCaseDetailResponse(
    AnimalCaseDetailDto animalCaseDetail,
    boolean isOwner,
    List<PendingProtectionResponse> pendingProtections
) {
  public static AnimalCaseDetailResponse create(
      AnimalCaseDetailDto animalCaseDetail,
      boolean isOwner,
      List<PendingProtectionResponse> pendingProtections
  ) {
    return new AnimalCaseDetailResponse(animalCaseDetail, isOwner, pendingProtections);
  }
}
