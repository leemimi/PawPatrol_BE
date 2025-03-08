package com.patrol.api.protection.dto;

import com.patrol.domain.member.member.enums.MemberRole;
import org.springframework.data.domain.Page;

public record MyAnimalCasePageResponse(
    Page<MyAnimalCaseResponse> page,
    MemberRole memberRole,
    long waitingCount,
    long protectingCount,
    long shelterCount
) {

  public static MyAnimalCasePageResponse create(
      Page<MyAnimalCaseResponse> page,
      MemberRole memberRole,
      long waitingCount,
      long protectingCount,
      long shelterCount
  ) {
    return new MyAnimalCasePageResponse(page, memberRole, waitingCount, protectingCount, shelterCount);
  }
}
