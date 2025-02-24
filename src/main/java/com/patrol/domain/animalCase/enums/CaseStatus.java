package com.patrol.domain.animalCase.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CaseStatus {

  UNKNOWN("불명"), // 최초 제보 시점에서 정확한 상태를 알 수 없는 경우
  MISSING("실종"), // 등록된 반려동물 실종 신고 상태
  RESCUE("구조"), // 등록된 반려동물 or 불명 강아지가 누군가에게 구조된 상태

  // 구조 후 선택
  TEMP_PROTECT_WAITING("임시보호 대기"), // 임시보호자를 기다리는 상태
  SHELTER_PROTECTING("보호소 보호 중"), // 보호소에서 보호 중인 상태
  TEMP_PROTECTING("임시보호 중"), // 임시보호자가 보호 중인 상태
  OWNER_FOUND("보호자 인계 완료"), // 원래 주인을 찾아 인계 완료된 상태
  ADOPTED("입양 완료"); // 새로운 가정으로 입양 완료된 상태


  private final String description;


  public boolean isTerminalStatus() {
    return this == OWNER_FOUND || this == ADOPTED;
  }

  public boolean isTempProtectible() {
    return this == TEMP_PROTECT_WAITING || this == TEMP_PROTECTING;
  }
}
