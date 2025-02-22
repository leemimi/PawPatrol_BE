package com.patrol.domain.protection.animalCase.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CaseHistoryStatus {
  // 실종 관련
  INITIAL_MISSING_REPORT("최초 실종 신고"), // 처음 실종 신고했을 때
  ADDITIONAL_MISSING_REPORT("추가 실종 신고"), // 이미 신고된 실종 건에 추가 신고할 때

  // 발견/구조 관련
  FOUND_REPORT("발견 제보"), // 누군가 발견했다고 제보
  RESCUE_REPORT("구조 제보"), // 누군가 구조했다고 제보
  FACILITY_REQUEST("병원 연계 요청"), // 병원/보호소 연계 요청

  // 임시보호 관련
  TEMP_PROTECT_REQUEST("임시보호 신청"), // 임시보호 신청
  TEMP_PROTECT_APPROVED("임시보호 승인"), // 임시보호 승인
  TEMP_PROTECT_START("임시보호 시작"), // 임시보호 시작
  TEMP_PROTECT_END("임시보호 종료"), // 임시보호 종료

  // 종료 관련
  OWNER_FOUND("보호자 발견"), // 원래 주인 찾음
  ADOPTED("입양 완료"), // 새로운 가정에 입양
  TRANSFERRED_TO_SHELTER("보호소/병원 이관"); // 보호소로 이관

  private final String description;

}
