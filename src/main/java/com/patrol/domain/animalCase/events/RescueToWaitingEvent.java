package com.patrol.domain.animalCase.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RescueToWaitingEvent {
  private final Long caseId;
  private final Long memberId;
  private final Long findPostId;
}
