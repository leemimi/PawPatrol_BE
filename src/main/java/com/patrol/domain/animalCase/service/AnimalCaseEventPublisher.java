package com.patrol.domain.animalCase.service;


import com.patrol.domain.animal.entity.Animal;
import com.patrol.domain.animalCase.enums.CaseHistoryStatus;
import com.patrol.domain.animalCase.enums.CaseStatus;
import com.patrol.domain.animalCase.enums.ContentType;
import com.patrol.domain.animalCase.events.PostCreatedEvent;
import com.patrol.domain.animalCase.events.AnimalCaseCreatedEvent;
import com.patrol.domain.animalCase.events.ProtectionStatusChangeEvent;
import com.patrol.domain.lostFoundPost.entity.LostFoundPost;
import com.patrol.domain.lostFoundPost.entity.PostStatus;
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


  public void createLostFoundPost(LostFoundPost lostFoundPostPost) {
    if (lostFoundPostPost.getStatus().equals(PostStatus.FINDING)) {
      eventPublisher.publishEvent(new PostCreatedEvent(
          ContentType.LOSTPOST, lostFoundPostPost.getId(),
          lostFoundPostPost.getPet().getId(), lostFoundPostPost.getAuthor().getId()
      ));
    }

    eventPublisher.publishEvent(new PostCreatedEvent(
        ContentType.FINDPOST, lostFoundPostPost.getId(),
        lostFoundPostPost.getPet().getId(), lostFoundPostPost.getAuthor().getId()
    ));
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

  public void createAnimalCase(Member member, Animal animal) {
    eventPublisher.publishEvent(new AnimalCaseCreatedEvent(
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
