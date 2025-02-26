package com.patrol.domain.protection.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProtectionType {

  TEMP_PROTECTION("임시보호"),
  ADOPTION("입양");

  private final String description;

}
