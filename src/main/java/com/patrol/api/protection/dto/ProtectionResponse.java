package com.patrol.api.protection.dto;


import com.patrol.domain.protection.entity.Protection;
import com.patrol.domain.protection.enums.ProtectionStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ProtectionResponse(
    String applicantName, String animalName, String reason,
    ProtectionStatus protectionStatus, LocalDateTime createdAt,
    String rejectReason
) {

  public static ProtectionResponse of(Protection protection) {
    return ProtectionResponse.builder()
        .applicantName(protection.getApplicant().getNickname())
        .animalName(protection.getAnimalCase().getAnimal().getName())
        .reason(protection.getReason())
        .createdAt(protection.getCreatedAt())
        .protectionStatus(protection.getProtectionStatus())
        .rejectReason(protection.getRejectReason())
        .build();
  }
}
