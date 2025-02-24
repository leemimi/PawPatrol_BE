package com.patrol.domain.animalCase.events;

import com.patrol.domain.animalCase.enums.CaseHistoryStatus;
import com.patrol.domain.animalCase.enums.CaseStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ProtectionStatusChangeEvent {
  private final Long protectionId;
  private final Long memberId;
  private final CaseStatus toStatus;
  private final CaseHistoryStatus historyStatus;
}
