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


  public void createLostFoundPost(LostFoundPost lostFoundPost) {
    PostStatus status = lostFoundPost.getStatus();
    ContentType contentType;
    if (status == PostStatus.FINDING) {
      contentType = ContentType.LOSTPOST;
    } else if (status == PostStatus.SIGHTED) {
      contentType = ContentType.FINDPOST;
    } else {
      return;
    }

    eventPublisher.publishEvent(new PostCreatedEvent(
        contentType, lostFoundPost.getId(),
        lostFoundPost.getPet().getId(), lostFoundPost.getAuthor().getId()
    ));
  }


  public void acceptProtection(Long protectionId, Long memberId, CaseStatus toStatus) {
    eventPublisher.publishEvent(new ProtectionStatusChangeEvent(
        protectionId, memberId,
        toStatus, CaseHistoryStatus.TEMP_PROTECT_APPROVED
    ));
  }


  public void rejectProtection(Long protectionId, Long memberId, CaseStatus toStatus) {
    eventPublisher.publishEvent(new ProtectionStatusChangeEvent(
        protectionId, memberId,
        toStatus, CaseHistoryStatus.TEMP_PROTECT_REJECTED
    ));
  }

  public void createAnimalCase(Member member, Animal animal, String title, String description) {
    eventPublisher.publishEvent(new AnimalCaseCreatedEvent(
        member, animal, title, description
    ));
  }

  public void applyProtection(Long protectionId, Long memberId, CaseStatus status) {
    eventPublisher.publishEvent(new ProtectionStatusChangeEvent(
        protectionId, memberId,
        status, CaseHistoryStatus.TEMP_PROTECT_REQUEST
    ));
  }
}
