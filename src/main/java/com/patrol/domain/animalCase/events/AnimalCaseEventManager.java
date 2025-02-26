package com.patrol.domain.animalCase.events;

import com.patrol.domain.animal.entity.Animal;
import com.patrol.domain.animal.service.AnimalService;
import com.patrol.domain.animalCase.entity.AnimalCase;
import com.patrol.domain.animalCase.enums.CaseHistoryStatus;
import com.patrol.domain.animalCase.enums.CaseStatus;
import com.patrol.domain.animalCase.enums.ContentType;
import com.patrol.domain.animalCase.service.AnimalCaseService;
import com.patrol.domain.animalCase.service.CaseHistoryService;
import com.patrol.domain.member.member.entity.Member;
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
  private final AnimalService animalService;
  private final ProtectionService protectionService;


  // PostCreated 이벤트 처리
  @Transactional
  public void handleLostPost(
      Long animalId, ContentType contentType, Long contentId, Long memberId
  ) {
    Animal animal = animalService.findById(animalId)
        .orElseThrow(() -> new CustomException(ErrorCode.ENTITY_NOT_FOUND));

    AnimalCase animalCase = getOrCreateAnimalCase(animal, CaseStatus.MISSING);
    boolean isNewCase = animalCase.getId() == null;
    if (isNewCase) {
      caseHistoryService.addInitialLostPost(animalCase, contentType, contentId, memberId);
    } else {
      caseHistoryService.addAdditionalLostPost(animalCase, contentType, contentId, memberId);
    }
  }


  @Transactional
  public void handleFindPost(
      Long animalId, ContentType contentType, Long contentId, Long memberId
  ) {
    Animal animal = animalService.findById(animalId)
        .orElseThrow(() -> new CustomException(ErrorCode.ENTITY_NOT_FOUND));

    AnimalCase animalCase = getOrCreateAnimalCase(animal, CaseStatus.UNKNOWN);
    caseHistoryService.addFindPost(animalCase, contentType, contentId, memberId);
  }


  private AnimalCase getOrCreateAnimalCase(Animal animal, CaseStatus status) {
    AnimalCase animalCase = animalCaseService.findByAnimal(animal);
    if (animalCase == null) {
      animalCase = animalCaseService.createNewCase(status, animal);
    }
    return animalCase;
  }



  // ProtectionStatusChange 이벤트 처리
  @Transactional
  public void handleProtectionStatusChange(
      Long protectionId, Long memberId, CaseStatus toStatus, CaseHistoryStatus historyStatus
  ) {
    Protection protection = protectionService.findById(protectionId)
        .orElseThrow(() -> new CustomException(ErrorCode.ENTITY_NOT_FOUND));

    AnimalCase animalCase = protection.getAnimalCase();

    // 유효성 검증
    validateStatusTransition(animalCase.getStatus(), toStatus);  // 상태 전이 가능 여부 (임보대기 -> 임보 중)

    animalCase.updateStatus(toStatus);
    caseHistoryService.addHistory(animalCase, historyStatus, ContentType.PROTECTION, protection.getId(), memberId);
  }


  private void validateStatusTransition(CaseStatus fromStatus, CaseStatus toStatus) {
    boolean isValid = switch (toStatus) {
      case TEMP_PROTECTING -> fromStatus.isTempProtectible();
      default -> false;
    };

    if (!isValid) {
      throw new CustomException(ErrorCode.INVALID_STATUS_CHANGE);
    }
  }


  // AnimalCaseCreated 이벤트 처리
  @Transactional
  public void handleAnimalCaseCreated(
      Animal animal, Member member, String title) {
    AnimalCase animalCase = animalCaseService.createNewCase(CaseStatus.PROTECT_WAITING, animal);
    animalCase.setCurrentFoster(member);
    animalCase.setTitle(title);
    caseHistoryService.addAnimalCase(
        animalCase, ContentType.ANIMAL_CASE, animalCase.getId(), member.getId()
    );
  }
}
