package com.patrol.api.protection.dto;

import com.patrol.domain.protection.entity.Protection;
import com.patrol.domain.protection.enums.ProtectionStatus;
import com.patrol.domain.protection.enums.ProtectionType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PendingProtectionResponse(
  Long protectionId, Long animalCaseId,
  String applicantName, String reason, ProtectionType protectionType,
  ProtectionStatus protectionStatus, LocalDateTime createdAt,
  LocalDateTime modifiedAt, String rejectReason
) {

  public static PendingProtectionResponse of(Protection protection) {
    return PendingProtectionResponse.builder()
        .protectionId(protection.getId())
        .animalCaseId(protection.getAnimalCase().getId())
        .applicantName(protection.getApplicant().getNickname())
        .reason(protection.getReason())
        .protectionType(protection.getProtectionType())
        .createdAt(protection.getCreatedAt())
        .modifiedAt(protection.getModifiedAt())
        .protectionStatus(protection.getProtectionStatus())
        .rejectReason(protection.getRejectReason())
        .build();
  }
}


