package com.patrol.api.animalCase.dto;


import com.patrol.domain.animalCase.entity.CaseHistory;
import com.patrol.domain.animalCase.enums.ContentType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CaseHistoryResponse(
    ContentType contentType, Long contentId,
    LocalDateTime createdAt, String statusDescription, Long memberId
) {

  public static CaseHistoryResponse of(CaseHistory caseHistory) {
    return CaseHistoryResponse.builder()
        .contentType(caseHistory.getContentType())
        .contentId(caseHistory.getContentId())
        .createdAt(caseHistory.getCreatedAt())
        .statusDescription(caseHistory.getHistoryStatus().getDescription())
        .build();
  }
}
