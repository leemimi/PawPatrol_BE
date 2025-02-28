package com.patrol.domain.protection.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProtectionStatus {

  PENDING("승인 대기"),
  CANCELED("취소됨"),
  APPROVED("임시 보호 승인"),
  REJECTED("임시 보호 거절됨");

  private final String description;
}
