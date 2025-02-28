package com.patrol.api.protection.dto;


import com.patrol.domain.protection.entity.Protection;
import com.patrol.domain.protection.enums.ProtectionStatus;
import com.patrol.domain.protection.enums.ProtectionType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ProtectionResponse(
    Long protectionId, Long animalCaseId,
    String applicantName, String animalName, String imageUrl, String reason,
    ProtectionType protectionType, ProtectionStatus protectionStatus, LocalDateTime createdAt,
    String rejectReason
) {

  public static ProtectionResponse of(Protection protection) {
    return ProtectionResponse.builder()
        .protectionId(protection.getId())
        .animalCaseId(protection.getAnimalCase().getId())
        .applicantName(protection.getApplicant().getNickname())
        .animalName(protection.getAnimalCase().getAnimal().getName())
        .imageUrl(protection.getAnimalCase().getAnimal().getImageUrl())
        .reason(protection.getReason())
        .createdAt(protection.getCreatedAt())
        .protectionType(protection.getProtectionType())
        .protectionStatus(protection.getProtectionStatus())
        .rejectReason(protection.getRejectReason())
        .build();
  }
}
