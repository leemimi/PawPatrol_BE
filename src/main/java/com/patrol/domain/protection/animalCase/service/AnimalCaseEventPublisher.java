package com.patrol.domain.protection.animalCase.service;

import com.patrol.domain.LostPost.entity.LostPost;
import com.patrol.domain.findPost.entity.FindPost;
import com.patrol.domain.protection.animalCase.enums.CaseStatus;
import com.patrol.domain.protection.animalCase.enums.ContentType;
import com.patrol.domain.protection.animalCase.enums.TargetType;
import com.patrol.domain.protection.animalCase.events.PostCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnimalCaseEventPublisher {

  private final ApplicationEventPublisher eventPublisher;


  public void lostPost(LostPost lostPost) {
    eventPublisher.publishEvent(new PostCreatedEvent(
        ContentType.LOSTPOST, lostPost.getLostId(),
        TargetType.MY_PET, lostPost.getPetId()
    ));
  }

  // MyPet 제보글
  public void findPost(FindPost findPost) {
    eventPublisher.publishEvent(new PostCreatedEvent(
        ContentType.FINDPOST, findPost.getId(),
        TargetType.MY_PET, findPost.getPetId()
    ));
  }

  // 제보글 중 MyPet이 아닌 Animal인 경우
  public void unknownFindPost(FindPost findPost) {
    eventPublisher.publishEvent(new PostCreatedEvent(
        ContentType.FINDPOST, findPost.getId(),
        TargetType.ANIMAL, findPost.getPetId()
    ));
  }
}
