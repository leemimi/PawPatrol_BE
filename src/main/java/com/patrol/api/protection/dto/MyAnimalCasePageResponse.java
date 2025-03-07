package com.patrol.api.protection.dto;

import org.springframework.data.domain.Page;

public record MyAnimalCasePageResponse(
    Page<MyAnimalCaseResponse> page,
    long waitingCount,
    long protectingCount
) {

  public static MyAnimalCasePageResponse create(
      Page<MyAnimalCaseResponse> page,
      long waitingCount,
      long protectingCount
  ) {
    return new MyAnimalCasePageResponse(page, waitingCount, protectingCount);
  }
}
