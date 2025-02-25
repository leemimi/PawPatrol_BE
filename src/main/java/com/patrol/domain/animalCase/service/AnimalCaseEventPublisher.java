package com.patrol.domain.animalCase.service;


import com.patrol.domain.animal.entity.Animal;
import com.patrol.domain.animalCase.enums.CaseHistoryStatus;
import com.patrol.domain.animalCase.enums.CaseStatus;
import com.patrol.domain.animalCase.enums.ContentType;
import com.patrol.domain.animalCase.events.PostCreatedEvent;
import com.patrol.domain.animalCase.events.ProtectionCreatedEvent;
import com.patrol.domain.animalCase.events.ProtectionStatusChangeEvent;
import com.patrol.domain.lostFoundPost.entity.LostFoundPost;
import com.patrol.domain.member.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnimalCaseEventPublisher {

  private final ApplicationEventPublisher eventPublisher;


  public void createLostPost(LostFoundPost lostPost) {
    eventPublisher.publishEvent(new PostCreatedEvent(
        ContentType.LOSTPOST, lostPost.getId(),
        lostPost.getPet().getId(), (long) 1
    ));

    //    eventPublisher.publishEvent(new PostCreatedEvent(
    //        ContentType.LOSTPOST, lostPost.getLostId(),
    //        TargetType.MY_PET, lostPost.getPetId()
    //    ));
  }

  // MyPet 제보글
  public void createfindPost(LostFoundPost findPost) {
    eventPublisher.publishEvent(new PostCreatedEvent(
        ContentType.FINDPOST, findPost.getId(),
        findPost.getPet().getId(), (long) 1
    ));

    //    eventPublisher.publishEvent(new PostCreatedEvent(
    //        ContentType.FINDPOST, findPost.getId(),
    //        TargetType.MY_PET, findPost.getPetId()
    //    ));
  }

  // 제보글 중 MyPet이 아닌 Animal인 경우
  public void createRescueFindPost(LostFoundPost findPost) {

    eventPublisher.publishEvent(new PostCreatedEvent(
        ContentType.FINDPOST, findPost.getId(),
        findPost.getPet().getId(), (long) 1
    ));

    //    eventPublisher.publishEvent(new PostCreatedEvent(
    //        ContentType.FINDPOST, findPost.getId(),
    //        TargetType.ANIMAL, findPost.getPetId()
    //    ));
  }


  public void acceptProtection(Long protectionId, Long memberId) {
    eventPublisher.publishEvent(new ProtectionStatusChangeEvent(
        protectionId, memberId,
        CaseStatus.TEMP_PROTECTING, CaseHistoryStatus.TEMP_PROTECT_APPROVED
    ));
  }


  public void rejectProjection(Long protectionId, Long memberId) {
    eventPublisher.publishEvent(new ProtectionStatusChangeEvent(
        protectionId, memberId,
        CaseStatus.TEMP_PROTECT_WAITING, CaseHistoryStatus.TEMP_PROTECT_REJECTED
    ));
  }

  public void createProtection(Member member, Animal animal) {
    eventPublisher.publishEvent(new ProtectionCreatedEvent(
        member, animal,
        CaseStatus.TEMP_PROTECT_WAITING, CaseHistoryStatus.TEMP_PROTECT_REGISTERED
    ));
  }

  public void applyProtection(Long protectionId, Long memberId, CaseStatus status) {
    eventPublisher.publishEvent(new ProtectionStatusChangeEvent(
        protectionId, memberId,
        status, CaseHistoryStatus.TEMP_PROTECT_REQUEST
    ));
  }
}
