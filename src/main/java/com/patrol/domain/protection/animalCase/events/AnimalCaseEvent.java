package com.patrol.domain.protection.animalCase.events;

import com.patrol.domain.protection.animalCase.enums.CaseStatus;
import com.patrol.domain.protection.animalCase.enums.ContentType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class AnimalCaseEvent {
  private final ContentType contentType;      // FINDPOST, LOSTPOST, PROTECTION
  private final Long contentId;     // findPost, lostPost, protection의 ID
}
