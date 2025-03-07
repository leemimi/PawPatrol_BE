package com.patrol.api.protection.dto;

import com.patrol.api.animalCase.dto.AnimalCaseDetailDto;
import com.patrol.domain.image.entity.Image;

import java.util.List;

public record AnimalCaseDetailResponse(
    AnimalCaseDetailDto animalCaseDetail,
    boolean isOwner,
    List<PendingProtectionResponse> pendingProtections,
    List<Image> images
) {
  public static AnimalCaseDetailResponse create(
      AnimalCaseDetailDto animalCaseDetail,
      boolean isOwner,
      List<PendingProtectionResponse> pendingProtections,
      List<Image> images
  ) {
    return new AnimalCaseDetailResponse(animalCaseDetail, isOwner, pendingProtections, images);
  }
}
