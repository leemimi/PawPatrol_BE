package com.patrol.domain.protection.animalCase.events;


import com.patrol.domain.protection.animalCase.enums.ContentType;
import com.patrol.domain.protection.animalCase.enums.TargetType;
import lombok.Getter;

@Getter
public class PostCreatedEvent extends AnimalCaseEvent {

  private final TargetType targetType;     // MY_PET, ANIMAL
  private final Long targetId;       // Animal or MyPet id


  public PostCreatedEvent(
      ContentType contentType, Long contentId,
      TargetType targetType, Long targetId
  ) {
    super(contentType, contentId);
    this.targetType = targetType;
    this.targetId = targetId;
  }
}
