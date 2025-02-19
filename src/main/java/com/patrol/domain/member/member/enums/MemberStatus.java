package com.patrol.domain.member.member.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberStatus {
  ACTIVE("정상"),
  INACTIVE("휴면"),
  BANNED("정지"),  // 정지
  WITHDRAWN("탈퇴");

  private final String description;

  public boolean canLogin() {
    return this == ACTIVE;
  }
}
