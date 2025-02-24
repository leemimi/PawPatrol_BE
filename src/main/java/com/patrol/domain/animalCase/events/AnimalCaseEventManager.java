package com.patrol.domain.animalCase.events;

import com.patrol.domain.animal.entity.Animal;
import com.patrol.domain.animal.service.AnimalService;
import com.patrol.domain.animalCase.entity.AnimalCase;
import com.patrol.domain.animalCase.enums.CaseHistoryStatus;
import com.patrol.domain.animalCase.enums.CaseStatus;
import com.patrol.domain.animalCase.enums.ContentType;
import com.patrol.domain.animalCase.service.AnimalCaseService;
import com.patrol.domain.animalCase.service.CaseHistoryService;
import com.patrol.domain.findPost.entity.FindPost;
import com.patrol.domain.findPost.repository.FindPostRepository;
import com.patrol.domain.protection.entity.Protection;
import com.patrol.domain.protection.service.ProtectionService;
import com.patrol.global.error.ErrorCode;
import com.patrol.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnimalCaseEventManager {

  private final AnimalCaseService animalCaseService;
  private final CaseHistoryService caseHistoryService;
  private final FindPostRepository findPostRepository;
  private final AnimalService animalService;
  private final ProtectionService protectionService;


  // PostCreatedEvent 처리
  @Transactional
  public void handleLostPostEvent(PostCreatedEvent event) {
    handleLostPost(
        event.getAnimalId(), event.getContentType(),
        event.getContentId(), event.getMemberId()
    );
  }


  @Transactional
  public void handleFindPostEvent(PostCreatedEvent event) {
    FindPost findPost = findPostRepository.findById(event.getContentId())
        .orElseThrow(() -> new CustomException(ErrorCode.ENTITY_NOT_FOUND));

    if (findPost.getStatus().equals(FindPost.Status.FOSTERING)) {
      handleRescueFindPost(
          event.getAnimalId(), event.getContentType(),
          event.getContentId(), event.getMemberId()
      );
    }

    handleFindPost(
        event.getAnimalId(), event.getContentType(),
        event.getContentId(), event.getMemberId()
    );
  }


  private void handleLostPost(
      Long animalId, ContentType contentType, Long contentId, Long memberId
  ) {
    Animal animal = animalService.findById(animalId)
        .orElseThrow(() -> new CustomException(ErrorCode.ENTITY_NOT_FOUND));

    AnimalCase animalCase = animalCaseService.findByAnimal(animal);
    if (animalCase == null) {
      animalCase = animalCaseService.createNewCase(CaseStatus.MISSING, animal);
      caseHistoryService.addInitialLostPost(animalCase, contentType, contentId, memberId);

    } else {
      caseHistoryService.addAdditionalLostPost(animalCase, contentType, contentId, memberId);
    }
  }

  private void handleFindPost(
      Long animalId, ContentType contentType, Long contentId, Long memberId
  ) {
    Animal animal = animalService.findById(animalId)
        .orElseThrow(() -> new CustomException(ErrorCode.ENTITY_NOT_FOUND));

    AnimalCase animalCase = animalCaseService.findByAnimal(animal);
    if (animalCase == null) {
      animalCase = animalCaseService.createNewCase(CaseStatus.UNKNOWN, animal);
      caseHistoryService.addFindPost(animalCase, contentType, contentId, memberId);

    } else {
      caseHistoryService.addFindPost(animalCase, contentType, contentId, memberId);
    }
  }


  private void handleRescueFindPost(
      Long animalId, ContentType contentType, Long contentId, Long memberId
  ) {
    Animal animal = animalService.findById(animalId)
        .orElseThrow(() -> new CustomException(ErrorCode.ENTITY_NOT_FOUND));

    AnimalCase animalCase = animalCaseService.findByAnimal(animal);
    if (animalCase == null) {
      animalCase = animalCaseService.createNewCase(CaseStatus.RESCUE, animal);
      caseHistoryService.addRescueFindPost(animalCase, contentType, contentId, memberId);

    } else {
      animalCase.updateStatus(CaseStatus.RESCUE);
      caseHistoryService.addRescueFindPost(animalCase, contentType, contentId, memberId);
    }
  }



  // ProtectionStatusChange 처리
  @Transactional
  public void updateStatus(
      Long protectionId, Long memberId, CaseStatus toStatus, CaseHistoryStatus historyStatus
  ) {
    Protection protection = protectionService.findById(protectionId)
        .orElseThrow(() -> new CustomException(ErrorCode.ENTITY_NOT_FOUND));

    AnimalCase animalCase = protection.getAnimalCase();

    // 유효성 검증
    validateStatusTransition(animalCase.getStatus(), toStatus);  // 상태 전이 가능 여부 (임보대기 -> 임보 중
    validateStatusChangeAuthority(animalCase, toStatus, memberId);  // 권한 검증

    animalCase.updateStatus(toStatus);
    caseHistoryService.addHistory(animalCase, historyStatus, ContentType.PROTECTION, protection.getId(), memberId);
  }

  private void validateStatusTransition(CaseStatus fromStatus, CaseStatus toStatus) {
    boolean isValid = switch (toStatus) {
      case TEMP_PROTECT_WAITING, SHELTER_PROTECTING -> fromStatus == CaseStatus.RESCUE;
      case TEMP_PROTECTING -> fromStatus.isTempProtectible();
      default -> false;
    };

    if (!isValid) {
      throw new CustomException(ErrorCode.INVALID_STATUS_CHANGE);
    }
  }

  private void validateStatusChangeAuthority(AnimalCase animalCase, CaseStatus toStatus, Long memberId) {
    if (toStatus.equals(CaseStatus.TEMP_PROTECT_WAITING)) {
      animalCaseService.validateRescuePostOwner(animalCase.getId(), memberId);
    }
  }
}
